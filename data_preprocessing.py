import numpy as np
import pandas as pd
from cvxopt import matrix, solvers
import cvxopt
import codecs
from time import time


# Ldata.txt 파일 생성 - 생성 후 주석처리
# f_mean = pd.read_csv('matrix_cp_mean.txt', sep='\t')
# f_Ldata = f_mean.sum().to_list()

# with open('Ldata.txt', 'w') as f:
#    for x in f_Ldata:
#        f.write(f'{x}\t')
# f.close()
# # print(f_Ldata)

f_point = codecs.open('point_2x0.txt', 'r', encoding='utf8')  # x_i(종목 i에 투자한 비율)의 상한 # 'r'모드(읽기모드)로 인코딩
f_return = codecs.open('matrix_cp_return.txt', 'r', encoding='utf8')  # 수익률
f_Ldata = codecs.open('Ldata.txt', 'r', encoding='utf8')  # cp_mean(random loss function의 평균 계수)을 칼럼썸한 파일.
f_vars = open('vars.txt', 'r')  # vars(각 시나리오에 사용되는 변수)와 같은 파일.

point_2x0 = []
point_08x = []
point_16x = []

cp_return_s = []
vars = []

cp_mean = np.zeros((3000, 1))  # 0으로 채워진 3000*1 행렬 만듦.

# point_2x0.txt 읽고 배열로 만드는 과정
# 3000 * 1 data

line_point = f_point.readline()
for i in range(0, 3000):
    line_point = f_point.readline()
    line_point = line_point.replace("\n", "")
    pushData_point = line_point.split('\t')
    point_2x0.append(float(pushData_point[1]))

# matrix_cp_return.txt 읽고 배열로 만든 후 행렬에 넣는 과정
line_return = f_return.readline()  # 텍스트 파일에 첫 번째 줄은 x1과 같은 값들이기 때문에 필요 없음.
line_return = f_return.readline()
line_return = line_return.replace("\n", "")
pushData_return = line_return.split('\t')  # 현재 pushData_return은 문자
for i in range(0, 3000):
    cp_return_s.append(float(pushData_return[i]))
cp_return = matrix(cp_return_s)  # lp를 solve하기 위해 matrix로 바꿔줌.

# Ldata.txt를 읽고 행렬에 넣는 과정. *Ldata.txt는 cp_mean을 미리 합쳐둔 파일임. 로딩 시간을 단축하기 위해 미리 생성함.
line_Ldata = f_Ldata.readline()
pushData_Ldata = line_Ldata.split('\t')
for i in range(0, 3000):
    cp_mean[i] = (float(pushData_Ldata[i]))
Cost = -matrix(cp_mean)  # Ldata의 값들이 모두 음수이므로 양수로 바꿔 줌, 목적함수 변수의 계수.

# vars.txt를 읽고 배열로 만드는 과정
start_point = 10000
end_point = 20000


for i in range(0, 80000):  # 8만개 해결. len(vars) = 80000

    line_vars = f_vars.readline()
    line_vars = line_vars.replace("\n", '')
    pushData_vars = line_vars.split('\t')
    temp = []  # vars라는 배열에 한번에 넣기 위해 편의상 생성한 임시 배열.
    for j in range(0, len(pushData_vars) - 1):
        temp.append(int(pushData_vars[j]))
    vars.append(temp)

f_point.close()
f_return.close()
f_Ldata.close()
f_vars.close()

f_result = open('result_10000_do.txt', mode='w', encoding='utf-8')  # 8만개의 lp 문제를 푼 데이터를 저장하는 파일

solve_times = []
total_result = []
# 8만개의 시나리오를 풀어서 result.txt에 저장하는 코드
re = 'status' + "\t" + 'primal objective'+"\t"+'gap'+"\t"+'solve_time'+"\t"+'number_of_var\t'+'x_index\t'+"x_value\n"
f_result.write(re)

for i in range(0, 80000):  # 8만개 해결. len(vars) = 80000
    # to-do
    result = []
    Obj = []  # 목적함수의 계수.
    return_s = []  # vars에 있는 변수의 수익률
    point = []  # 2x0 값
    constant = -0.0963007951203275  # 수익률이 0.096... 은 넘어야 함.

    for j in range(0, len(vars[i])):
        Obj.append(Cost[vars[i][j]])
        # Cost는 목적함수의 계수에 해당. vars에는 [0 1	2	3	4	5	6	7	8	9 ... ] 이런 식으로 각 줄마다 사용할 변수들이 표시되어 있음. 0은 x1 이런 식으로.
        # 만약 vars[0](첫 번째 시나리오에서 사용되는 변수) [11, 32, ...] 이런 식으로 되어있으면 그 시나리오에서는 x12, x33 ... 을 사용한다는 것.
        # 여기서 vars[0][0] = 11
        # 그러면 목적함수 값이 담긴 cost 리스트에서 해당 변수에 해당하는 값을 가져와야 되는데, index가 0번부터 시작이니까 x12의 cost는 cost[11]에 들어있을 것.
        return_s.append(cp_return_s[vars[i][j]])
        point.append(point_2x0[vars[i][j]])

    n = len(vars[i])

    Obj = matrix(Obj)
    return_s = matrix(return_s)
    left_coeff = np.concatenate((np.ones((1,n)), -np.ones((1, n)), -return_s.T,-np.eye(n, dtype=int), np.eye(n, dtype=int)), axis=0)
    left_coef = matrix(left_coeff)

    point = matrix(point)
    right_const = matrix([1, -1, constant, matrix(np.zeros((n, 1))), point]) #이 경우, constant가 이미 음수 값으로 주어졌기 때문에 constant에 따로 –를 붙이지 않는다. 

    start = time()  # 시작시간
    sol = solvers.lp(Obj, left_coef, right_const, solver='glpk')  # lp 문제 풀기

    solve_time = time() - start
    #solve_times.append(solve_time)  # 걸린 시간.

    #result['status'] = sol['status']  # optimal인지 feasible인지 여부
    #result['primal objective'] = sol['primal objective']  # 목적함수 값.
    #result['gap'] = sol['gap']  # gap
    #result['solve_time'] = solve_time  # solvers.lp의 결과값은 dictionary 형태로 되어있음.
    # 위 코드는 lp를 푼 결과를 저장하는 result에 solve_time이라는 키를 새로 추가하는 것.

    re = sol['status'] + "\t" + str(sol['primal objective'])+"\t"+str(sol['gap'])+"\t"+str(solve_time)+"\t"
    re += str(n) + "\t" # x-index
    for k in range(n):
        re += str(vars[i][k]) + "\t"


    if sol['x'] != None:
        #result['x'] = list(sol['x'])
        s_list = sol['x']
        for t in s_list:
            re += str(t) + "\t"
    else:
        #result['x'] = sol['x']  # 최적해값.
        re += str(sol['x']) + "\t"

    print(i + 1, '번째 LP Solved')
    f_result.write(re + "\n")
    # print(solve_time, " 초")
    #total_result.append(result)
    #f_result.write(str(result) + "\n")

    # print("최적 목적식 값들", sol['x'])

#times_mean = np.mean(solve_times)
f_result.close()

# -----------------------------------------------------LP 풀고 데이터 출력 완료-----------------------------------------------------#
