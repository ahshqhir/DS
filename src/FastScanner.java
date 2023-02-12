import java.io.*;
import java.util.StringTokenizer;

public class FastScanner {
    BufferedReader bufferedReader;
    StringTokenizer st;

    public FastScanner(InputStream inputStream) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String next() {
        return st.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public float nextFloat() {
        return Float.parseFloat(next());
    }

    public void getInput() throws IOException {
        st=new StringTokenizer(bufferedReader.readLine());
    }

    public void Close() throws IOException {
        bufferedReader.close();
    }
}
