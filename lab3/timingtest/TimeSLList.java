package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        timeGetLast(Ns, times, opCounts);
        printTimingTable(Ns, times, opCounts);
    }

    public static void timeGetLast(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        int N = 1000;
        for (int i = 0; i < 8; i++) {
            SLList<Integer> sll = new SLList<>();
            Ns.addLast(N);
            int M = 10000;
            opCounts.addLast(M);
            for (int j = 0; j < N; j++) {
                sll.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int c = 0; c < M; c++) {
                int s = sll.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);

            N = N * 2;


        }
    }

}
