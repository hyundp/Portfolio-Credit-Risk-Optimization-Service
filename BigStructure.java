import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BigStructure {
    String status; // optimal check
    float primal_obj; // primal obj
    float gap; // gap
    float solve_time; // solve time

    ArrayList<Integer> sol_key = new ArrayList<>(); // 결정 변수
    ArrayList<Float> sol_val = new ArrayList<>(); // 결정 변수의 해값
    
    public BigStructure(){}
    //feasible의 경우
    public BigStructure(String status, float primal_obj, float solve_time, ArrayList<Integer> sol_key, ArrayList<Float> sol_val){
        this.status = status;
        this.primal_obj= primal_obj;
        this.solve_time = solve_time;
        this.sol_key = sol_key;
        this.sol_val = sol_val;
    }
    // infeasible인 경우
    public BigStructure(String status, float solve_time, ArrayList<Integer> sol_key){
        this.status = status;
        this.primal_obj= primal_obj;
        this.solve_time = solve_time;
        this.sol_key = sol_key;
    }



}
