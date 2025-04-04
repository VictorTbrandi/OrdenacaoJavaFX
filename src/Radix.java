import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Radix {
    int[] vet;
    int tl;

    public Radix() {
        vet = new int[10];
        tl = 0;
        Random rand = new Random();
        HashSet<Integer> valores = new HashSet<>();
        while (valores.size() < 10) {
            int num = rand.nextInt(10) + 1;
            if (valores.add(num))
                vet[tl++] = num;
        }
    }

    public void setVet(int[] vet) {
        this.vet = vet;
    }

    private int max(){
        int max = vet[0];
        for (int i = 1; i < vet.length; i++)
            if(vet[i] > max)
                max = vet[i];
        return max;
    }

    public void radixSort(){
        int max = max();
        int[] vet_ordenado = new int[tl];
        for (int dgt = 1; dgt <= max; dgt *= 10) {
            int[] counting = new int[10];
            for (int i = 0; i < tl; i++)
                counting[(vet[i] / dgt) % 10]++;

            for (int i = 1; i < 10; i++)
                counting[i] += counting[i - 1];

            for (int i = tl - 1; i >= 0; i--) {
                int pos = (vet[i] / dgt) % 10;
                vet_ordenado[counting[pos] - 1] = vet[i];
                counting[pos]--;
            }
            int[] aux = vet;
            vet = vet_ordenado;
            vet_ordenado = aux;
        }
    }

    public void combSort(){
        int intervalo = (int) (tl/1.3);
        boolean flag = true;

        while(intervalo > 1 || flag){
            flag = false;
            for (int i = intervalo; i < tl; i++)
                if(vet[i] < vet[i-intervalo]){
                    int aux = vet[i];
                    vet[i] = vet[i-intervalo];
                    vet[i-intervalo] = aux;
                    flag = true;
                }
            intervalo = Math.max((int) (intervalo / 1.3), 1);
        }
    }

    public void exibeVet(){
        System.out.println(Arrays.toString(vet));
    }
    public static void main(String[] args) {
        Radix radix = new Radix();

        radix.exibeVet();
        radix.radixSort();
        radix.exibeVet();
    }
}

