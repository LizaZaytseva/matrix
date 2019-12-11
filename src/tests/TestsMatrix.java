package tests;
import org.junit.Test;
import sample.Logic;

import static org.junit.jupiter.api.Assertions.*;

public class TestsMatrix {
    Logic logic = new Logic();
    Logic logic2 = new Logic();
    Logic logic3 = new Logic();
    String res = "";
    String res2 = "";
    //матрица с 1 возможным путём
    int[][] arr = new int[][]{
            {1, 2, 2},
            {2, 1, 3}
    };
    //матрица без возможных путей
    int[][] arr2 = new int[][]{
            {10, 12, 13, 14},
            {2, 1, 3, 5},
            {3, 2, 2, 2},
            {2, 6, 3, 2},
            {1, 2, 2, 6},
            {2, 1, 3, 1}
    };
    //матрица с 6 возможными путями
    int[][] arr3 = new int[][]{
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    @Test
    public void LogicTests(){
        assertEquals(0, logic2.result(arr2, 0, 0, 6,4, res2).size() );
        assertEquals("[1 4 6]", logic.result(arr, 0, 0, 2,3, res).toString());
        assertEquals( "[1 4 7 8 9, 1 4 5 8 9, 1 4 5 6 9, 1 2 5 8 9, 1 2 5 6 9, 1 2 3 6 9]", logic3.result(arr3, 0, 0, 3,3, res2).toString());
    }

}
