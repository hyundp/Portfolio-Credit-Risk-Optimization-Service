import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmallStructure {
    String status; // optimal check
    float primal_obj; // primal obj
    float gap; // gap
    float solve_time; // solve time

    ArrayList<Integer> sol_key = new ArrayList<>(); // 결정 변수
    
    //feasible의 경우
    public SmallStructure(String status, float primal_obj, float solve_time, ArrayList<Integer> sol_key){
        this.status = status;
        this.primal_obj= primal_obj;
        this.solve_time = solve_time;
        this.sol_key = sol_key;
    }
    // infeasible인 경우
    public SmallStructure(String status, float solve_time, ArrayList<Integer> sol_key){
        this.status = status;
        this.primal_obj= primal_obj;
        this.solve_time = solve_time;
        this.sol_key = sol_key;
    }
}
