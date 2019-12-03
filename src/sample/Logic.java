package sample;

import java.util.ArrayList;

public class Logic {
    private ArrayList<String> finish = new ArrayList<>();

    public ArrayList<String> result(int[][] arr, int startA, int startB, int str, int stb, String res){
        int x = arr[startA][startB]; //значение элемента матрицы = > длина шага
        int num = stb * (startA) + startB + 1; //вычисление порядкого номера элемента в листе
        int f = str * stb; //клетка окончания программы
        String st = String.valueOf(num);

        //проверяем, можно ли сделать ход вниз
        if (startA + x < str) {
            //записываем промежуточный результат
            res+= st + " ";
            //если возможен ход в итоговую клетку, то записываем результат
            if (startA + x == str - 1 && startB == stb - 1) {
                res += String.valueOf(f);
            }
            //если ход в итоговую клетку невозможен, запускаем наш метод в клетке, в которую осуществлен переход
            else result(arr, startA + x, startB, str, stb, res);
        }
        //проверяем, можно ли сделать ход вправо

        if (startB + x < stb) {
            //записываем промежуточный результат
            res+= st + " ";
            //если возможен ход в итоговую клетку, то записываем результат
            if (startB + x == stb - 1 && startA == str - 1) {
                res += String.valueOf(f);
            }
            //если ход в итоговую клетку невозможен, запускаем наш метод в клетке, в которую осуществлен переход
            else result(arr, startA, startB + x, str, stb, res);
        }
        int s = 0;
        if (!res.isEmpty()) {
            int splL = res.split(" ").length;
            s = Integer.valueOf(res.split(" ")[splL - 1]);
        }
        //проверяем, является ли последний ход, ходом в финальную клетку
        if (f == s) {
            String[] r = res.split(" ");
            //проверяем на повтор промежуточные точки, повторяющиеся удаляем => последствие логики поиска ходов
            for (int i = 0; i < r.length - 1; i++) {
                if (r[i].equals(r[i + 1])) {
                    res = res.replaceFirst(r[i] + " ", "");
                }
            }
            finish.add(res);
        }
        return finish;
    }
}
