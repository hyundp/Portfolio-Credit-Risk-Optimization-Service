import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class Main {
    final static int num = 80000;
    public static int return_num() { // 0~80000 사이의 문제 번호 값을 입력
        try {
            Scanner n = new Scanner(System.in);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("문제의 번호를 입력해 주세요. ");
            int problem_num = n.nextInt();
            if (problem_num > 0 && problem_num <= 80000) {
                return problem_num;
            } else {
                System.out.println("--------------------------------------------------------------------");
                System.out.print("잘못된 번호를 입력하셨습니다. \n 다시 입력해 주세요. ");
                return return_num();
            }
        } catch (Exception e){
            //정수가 아닌 경우를 입력받았을 경우 재입력을 위한 예외처리
            System.out.println("잘못된 값을 입력했습니다. \n ");
            return return_num();
        }
    }
    public static int return_xvars(){ // 결정 변수의 값을 입력받는 부분, return 0 to 2999
        try {
            Scanner scanner1 = new Scanner(System.in);
            System.out.print("x0 ~ x2999 사이 결정변수를 입력해 주세요 (x를 포함하여 입력): "); // x0 ~ x2999

            String s = scanner1.next();

            String s2 = s.substring(1);
            int nn = Integer.parseInt(s2);
            return nn;

        } catch (Exception e) {
            System.out.print("잘못된 문자를 입력했습니다. \n 다시 입력해 주세요. ");
            return return_xvars();
        }
    }

    public static void main(String[] args) throws IOException {

        long start_time = System.currentTimeMillis();
        Scanner scanner = new Scanner(new File("result_80000.txt"));

        String r1 = scanner.nextLine(); // 변수 이름부
        ArrayList<Integer> list_key;
        ArrayList<Float> list_val;
  // start read

        //int num = 80000; // 문제 갯수

        BigStructure[] sol = new BigStructure[num];
        String[] r;
        for (int i = 0; i < num; i++) {
            System.out.println("this num: "+i);
            String r2 = null;
            r = null;

            r2 = scanner.nextLine();
            list_key = new ArrayList<>();
            list_val = new ArrayList<>();

            r = r2.split("\t");
            int n = Integer.parseInt(r[4]); // n : 각 문제에서의 결정 변수들의 갯수

            // 결정변수와 그에 해당하는 값들을 저장
            if (Objects.equals(r[0], "optimal")) {
                //status	primal objective	gap	   solve_time	number_of_var	Vars	    value
                for (int j = 0; j < n; j++) {
                    list_key.add(Integer.parseInt(r[5 + j]));
                    list_val.add(Float.parseFloat(r[5 + j + n]));
                }
                sol[i] = new BigStructure(r[0], Float.parseFloat(r[1]), Float.parseFloat(r[3]), list_key, list_val);
            } else {
                for (int j = 0; j < n; j++) {
                    list_key.add(Integer.parseInt(r[5 + j]));

                }
                sol[i] = new BigStructure(r[0], Float.parseFloat(r[3]), list_key);

            }
        }   // result.txt 읽기 수행

        list_key = null;
        list_val = null;

        long end_time = System.currentTimeMillis();


        // ---------프로그램 실행 메인 --------------------------
        boolean program_on = true;
        int main_choice;

        float load_time =  (end_time - start_time)/1000f;
        System.out.println("로딩시간 : " + load_time + "초");

        while (program_on) {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("1. 문제의 풀이 시간");
            System.out.println("2. 문제가 풀렸는지 여부"); // 문제가 풀렸을 시 2-1 : 최적 목적식 값, 2-2 : 최적 해 값들
            System.out.println("3. 평균 걸린 시간 ");
            System.out.println("4. 결정 변수가 포함된 문제들의 평균 걸린 시간");
            System.out.println("5. 결정 변수가 포함된 문제들 중 풀린 문제와 그렇지 못한 경우");
            System.out.println("6. 결정 변수가 포함된 문제들 중 풀린 문제의 목적식 값들");
            System.out.println("7. 결정 변수가 포함된 문제들 중 풀린 문제의 최적 해 값들");
            System.out.println("8. 목적식 값이 입력 값 이하인 문제 출력 ");
            System.out.println("9. 결정변수의 최적해 값이 특정 조건을 만족하는 문제");
            System.out.println("0. 프로그램 종료");
            System.out.println("--------------------------------------------------------------------");
            System.out.print("선택 :  ");

            Scanner n = new Scanner(System.in);
            try {

                main_choice = n.nextInt();

                System.out.println("--------------------------------------------------------------------");

                int sol_n; // sol_n 은 문제 번호, index는 sol_n - 1을 해 주어야 함.
                if (main_choice == 1) {
                    sol_n = return_num();
                    System.out.println("문제의 풀이 시간 : " + sol[sol_n - 1].solve_time);
                } else if (main_choice == 2) {
                    sol_n = return_num();
                    System.out.println(sol_n +"번 문제 : " + sol[sol_n - 1].status);
                    if (sol[sol_n-1].status.equals("optimal")) {
                        System.out.println("최적 목적식 값 : " + sol[sol_n - 1].primal_obj);

                        String file_name = "result_"+"p_2_"+sol_n+".txt";
                        FileWriter fw2 = new FileWriter(file_name);
                        //System.out.print("최적 해 값 : ");
                        //System.out.print(val);
                        fw2.write(sol[sol_n-1].sol_val.toString());
                        fw2.close();
                        System.out.println("최적 해 값들이 "+file_name+"에 저장되었습니다.");

                    }
                } else if (main_choice == 3) {

                    float sum = 0f;
                    for (int i = 0; i < num; i++) {
                        sum += sol[i].solve_time;
                    }
                    System.out.println("문제의 평균 풀이 시간 : " + sum / num);

                } else if (main_choice == 4) {
                    int key = return_xvars();
                    float time = 0;
                    int cnt = 0;
                    for (int i = 0; i < num; i++) {
                        if (sol[i].sol_key.contains(key)) {
                            time += sol[i].solve_time;
                            cnt++;
                        }
                    }
                    System.out.println("x" + key + "가 포함된 문제들의 평균 시간 : " + time / cnt + "초");
                } else if (main_choice == 5) {
                    int key = return_xvars();
                    String file_name = "result_"+"p_"+main_choice+"_"+"x"+key+".txt";
                    FileWriter fw5 = new FileWriter(file_name);

                    ArrayList solved = new ArrayList();
                    ArrayList unsolved = new ArrayList();

                    int solved_num = 0;
                    int unsolved_num = 0;

                    for (int i = 0; i < num; i++) {
                        if (sol[i].sol_key.contains(key)) {
                            if (sol[i].status.equals("optimal")) {
                                solved.add(i + 1);
                                solved_num++;
                            } else {
                                unsolved.add(i + 1);
                                unsolved_num++;
                            }
                        }
                    }
                    String str_solved = solved.toString();
                    String str_unsolved=  unsolved.toString();

                    fw5.write(str_solved + "\n");
                    fw5.write(str_unsolved+ "\n");

                    System.out.println("x" + key + "가 포함된 문제 중 풀린 문제: " + solved_num+"개");
                    //System.out.println(str_solved);
                    System.out.println("x" + key + "가 포함된 문제 중 풀리지 않은 문제: " + unsolved_num+"개");
                    //System.out.println(str_unsolved);

                    fw5.close();
                    System.out.println("결정 변수가 포함된 문제들 중 풀린 문제와 그렇지 못한 경우가 ");
                    System.out.println("텍스트 파일"+file_name+"에 저장되었습니다.");

                } else if (main_choice == 6 || main_choice == 7) {

                    int key = return_xvars();
                    int cnt = 0;

                    String file_name = "result_"+"p_"+main_choice+"_"+"x"+key+".txt";
                    FileWriter fw = new FileWriter(file_name);
                    for (int i = 0; i < num; i++) {
                        if (sol[i].sol_key.contains(key) && sol[i].status.equals("optimal")) {
                            cnt++;
                            if (main_choice == 6) {
                                String str = "x" + key + "가 포함된 문제 중 풀린 문제 번호: " + (i + 1) + "의 목적식 값 -> " + sol[i].primal_obj;
                                fw.write(str+"\n");
                                //System.out.println(str);
                            }
                            if (main_choice == 7) {
                                String str = "x" + key + "가 포함된 문제 중 풀린 문제 번호: " + (i + 1) + "의 최적해 값들 -> " + sol[i].sol_val;
                                fw.write(str+"\n");
                                //System.out.println(str);

                            }
                        }
                    }

                    System.out.println("풀린 문제의 갯수 : " + cnt);
                    if(main_choice==6){
                        System.out.println("결정 변수가 포함된 문제들 중 풀린 문제의 목적식 값들이 ");
                    } else{
                        System.out.println("결정 변수가 포함된 문제들 중 풀린 문제의 최적 해 값들이 ");
                    }
                    System.out.println("텍스트 파일"+file_name+"에 저장되었습니다.");
                    fw.close();

                } else if (main_choice == 8) {
                    System.out.print("목적식 값 <= ");
                    Scanner scanner8 = new Scanner(System.in);
                    float num8 = scanner8.nextFloat();
                    int cnt = 0;
                    if(num8 > 0){
                        ArrayList condition_satiesfied = new ArrayList();
                        for (int i = 0; i < num; i++) {
                            if (sol[i].primal_obj <= num8 && sol[i].primal_obj > 0) {
                                condition_satiesfied.add(i + 1);
                                cnt++;
                            }
                        }
                        System.out.println("조건을 만족하는 문제: " + cnt +"개");
                        String str = condition_satiesfied.toString();

                        String file_name = "result_"+"p_"+main_choice+".txt";
                        FileWriter fw8 = new FileWriter(file_name, true); // 기존 txt파일에 내용을 추가
                        fw8.write("목적식 값 <= " + num8 + "\n");
                        fw8.write(str + "\n");
                        System.out.println("목적식 값 <=" + num8+" 의 문제번호가 ");
                        System.out.println("텍스트 파일"+file_name+"에 저장되었습니다.");

                        fw8.close();
                    }else System.out.println("None");

                } else if (main_choice == 9) {

                    Scanner scanner9 = new Scanner(System.in);
                    int nn = return_xvars();
                    System.out.print("부등호를 선택하세요. < or >  :   ");
                    String sign = scanner9.next();
                    System.out.print(" 범위를 입력 하세요."); // ex ) 1
                    double range = scanner9.nextDouble();
                    System.out.print("선택된 범위 : ");
                    System.out.println("x" + nn + " " + sign + " " + range); // x3 > 0.00001
                    int cnt = 0;

                    ArrayList s = new ArrayList();

                    for (int i = 0; i < num; i++) {
                        if (sol[i].sol_key.contains(nn) && sol[i].status.equals("optimal")) {
                            int index = sol[i].sol_key.indexOf(nn);

                            if (sol[i].sol_val.get(index) > range && sign.equals(">")) {
                                s.add(i+1);
                                //System.out.print(i+1 + " ");
                                cnt++;
                            }
                            else if (sol[i].sol_val.get(index) < range && sign.equals("<")) {
                                //System.out.print(i+1 + " ");
                                s.add(i+1);
                                cnt++;
                            }
                        }
                    }
                    System.out.println(" ");
                    System.out.println("해당하는 문제들의 갯수 : " + cnt);

                    String ss = s.toString();
                    String str = "선택된 범위 : " + "x" + nn + " " + sign + " " + range;
                    String file_name = "result_"+"p_"+main_choice+".txt";
                    FileWriter fw9 = new FileWriter(file_name, true);

                    fw9.write(str+"\n");
                    fw9.write(ss+"\n");

                    System.out.println(str + " 을 만족하는 문제번호가 ");
                    System.out.println("텍스트 파일"+file_name+"에 저장되었습니다.");

                    fw9.close();


                } else if (main_choice == 0) {
                    program_on = false;
                } else {
                    System.out.println("다시 입력해 주세요. ");
                }
            } catch (Exception e){
                System.out.println("잘못된 값을 입력했습니다. \n");
                System.out.println(e.getMessage());
            }
        }
    }
}
