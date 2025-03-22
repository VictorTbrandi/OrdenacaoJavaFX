import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Radix {
    int[] vet;

    public Radix() {
        vet = new int[10];
    }
    public void preencheVetor(){
        Random rand = new Random();
        HashSet<Integer> valores = new HashSet<>();
        int i = 0;
        while (valores.size() < 10) {
            int num = rand.nextInt(10);
            if (valores.add(num+1))
                vet[i++] = num + 1;
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
        for (int dgt = 1; max/dgt > 0 ; dgt *= 10) {
            int[] counting = new int[10];
            int[] vet_ordenado = new int[vet.length];

            for (int i = 0; i < vet.length; i++)
                counting[(vet[i] / dgt) % 10]++;

            for (int i = 1; i < counting.length; i++)
                counting[i] += counting[i - 1];

            for (int i = vet.length - 1; i >= 0; i--) {
                int pos = (vet[i] / dgt) % 10;
                vet_ordenado[counting[pos] - 1] = vet[i];
                counting[pos]--;
            }
            setVet(vet_ordenado);
        }
    }

    public void exibeVet(){
        System.out.println(Arrays.toString(vet));
    }
    public static void main(String[] args) {
        Radix radix = new Radix();
        radix.preencheVetor();
        System.out.println("Randomico");
        radix.exibeVet();
        radix.radixSort();
        System.out.println("Ordenado");
        radix.exibeVet();
    }
}

