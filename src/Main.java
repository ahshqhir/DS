import java.io.IOException;

public class Main {
    public static class Score {
        private float sum;
        private int num;
        private float average = -1;
        public Score() {
            this.sum = 0;
            this.num = 0;
        }
        public void add(float score) {
            num++;
            sum += score;
        }
        public float average() {
            if (average == -1)
                average = num == 0? 0: sum / num;
            return average;
        }
    }
    public static int a, b, p;
    private static Set[] relationsC;
    private static Set[] relationsS;
    private static Set terms;
    private static Set[] STerms;
    private static ArrayDict<Integer, Score>[] scores;
    private static ArrayDict<Integer, Integer> termArray1;
    private static ArrayDict<Integer, Integer> termArray2;
    private static int[] newTerm;
    private static ArrayDict<Integer, Set> newTerm2;
    private static int max = 0;
    public static void main(String[] args) throws IOException {
        Object index;
        Student std;
        Course crs;
        boolean initialized1 = false;
        boolean initialized2 = false;
        boolean initialized3 = false;

        FastScanner scan = new FastScanner(System.in);
        scan.getInput();
        int T = scan.nextInt();
        a = scan.nextInt();
        b = scan.nextInt();
        p = scan.nextInt();
        for (; T > 0; T--) {
            scan.getInput();
            String s = scan.next();
            switch (s) {
                case "ADDS":
                    new Student(scan.next(), scan.next());
                    break;
                case "ADDC":
                    new Course(scan.next(), scan.next());
                    break;
                case "ADDG":
                    new Grade(scan.next(), scan.next(), scan.nextInt(), scan.nextFloat());
                    break;
                case "EDITS":
                    Student.students.getK(scan.next()).setName(scan.next());
                    break;
                case "EDITC":
                    Course.courses.getK(scan.next()).setName(scan.next());
                    break;
                case "EDITG":
                    Student.students.getK(scan.next()).grades.getK(scan.next()).score = scan.nextFloat();
                    break;
                case "DELETES":
                    index = Student.students.getIndexK(scan.next());
                    Student.students.get(index).delete();
                    Student.students.remove(index);
                    break;
                case "DELETEC":
                    index = Course.courses.getIndexK(scan.next());
                    Course.courses.get(index).delete();
                    Course.courses.remove(index);
                    break;
                case "DELETEG":
                    std = Student.students.getK(scan.next());
                    index = std.grades.getIndexK(scan.next());
                    std.grades.get(index).deleteC();
                    std.grades.remove(index);
                    break;
                case "NUMBERC":
                    System.out.println(Student.students.getK(scan.next()).grades.getLength());
                    break;
                case "NUMBERS":
                    System.out.println(Course.courses.getK(scan.next()).grades.getLength());
                    break;
                case "SEARCHSN":
                    Student.students.search(scan.next()).print();
                    break;
                case "SEARCHCN":
                    Course.courses.search(scan.next()).print();
                    break;
                case "SEARCHSC":
                    std = Student.students.getK(scan.next());
                    System.out.println(std.hashCode());
                    std.print();
                    break;
                case "SEARCHCC":
                    crs = Course.courses.getK(scan.next());
                    System.out.println(crs.hashCode());
                    crs.print();
                    break;
                case "ISRELATIVE":
                    if (!initialized1) {
                        initialize1();
                        initialized1 = true;
                    }
                    System.out.println(relationsC[(int) Course.courses.getIndexK(scan.next())].contains(scan.nextInt())?
                            "yes": "no");
                    break;
                case "ALLRELATIVE":
                    if (!initialized1) {
                        initialize1();
                        initialized1 = true;
                    }
                    for (int id: relationsC[(int) Course.courses.getIndexK(scan.next())])
                        if (id != -1)
                            System.out.print(id + " ");
                    System.out.println();
                    break;
                case "COMPARE":
                    if (!initialized3) {
                        initialize3();
                        initialized3 = true;
                    }
                    int i1 = (int) Student.students.getIndexK(scan.next());
                    int i2 = (int) Student.students.getIndexK(scan.next());
                    boolean b1 = relationsS[i1].contains(i2);
                    boolean b2 = relationsS[i2].contains(i1);
                    if (b1 && !b2)
                        System.out.println(">");
                    else if (b2 && !b1)
                        System.out.println("<");
                    else
                        System.out.println("?");
                    break;
                case "MINRISK":
                    if (!initialized2) {
                        initialize2();
                        initialized2 = true;
                    }
                    std = Student.students.getK(scan.next());
                    termArray1 = new ArrayDict<>();
                    Set termSet = new Set(-1, null);
                    for (IDict.Entry<String, Grade> grade: std.grades) {
                        Grade g = grade.getValue();
                        if (termSet.add(g.term))
                            termArray1.add(g.term, 1);
                        else {
                            index = termArray1.getIndexK(g.term);
                            termArray1.set(index, termArray1.get(index) + 1);
                        }
                    }
                    best(std, -1);
                    for (int t: termSet) {
                        if (t == -1)
                            continue;
                        System.out.print(t + " ");
                        for (int c: newTerm2.getK(t))
                            if (c != -1)
                                System.out.print(c + " ");
                        System.out.println();
                    }
                    break;
            }
        }
    }

    private static void best(Student std, int d) {
        if (d < 0) {
            termArray2 = new ArrayDict<>();
            for (IDict.Entry<Integer, Integer> entry: termArray1)
                termArray2.add(entry.getKey(), 0);
            newTerm = new int[std.grades.getLength()];
            best(std, 0);
            max = 0;
        }
        else if (d < std.grades.getLength()) {
            int i = std.grades.get(d).index;
            int index;
            for (int j: STerms[i]) {
                index = termArray2.getIndexK(j);
                if (index == -1)
                    continue;
                newTerm[d] = j;
                termArray2.set(index, termArray2.get(index) + 1);
                best(std, d + 1);
                termArray2.set(index, termArray2.get(index) - 1);
            }
        }
        else {
            int ln = termArray1.getLength();
            for (int i = 0; i < ln; i++)
                if (termArray1.get(i).intValue() != termArray2.get(i).intValue())
                    return;
            int sum = 0;
            for (int i = 0; i < newTerm.length; i++) {
                sum += scores[std.grades.get(i).index].getK(newTerm[i]).average();
            }
            if (sum > max) {
                max = sum;
                copy(std);
            }
        }
    }

    private static void copy(Student std) {
        if (termArray1 == null || newTerm == null)
            return;
        newTerm2 = new ArrayDict<>();
        for (int i = 0; i < newTerm.length; i++) {
            Set s = newTerm2.getK(newTerm[i]);
            if (s == null) {
                s = new Set(-1, null);
                newTerm2.add(newTerm[i], s);
            }
            s.add(Integer.parseInt(std.grades.get(i).course.ID));
        }
    }

    private static void initialize1() {
        int ln = Course.courses.getLength();
        Set[] exact = new Set[ln];
        for (int i = 0; i < ln; i++) {
            exact[i] = new Set(-1, null);
            Course c1 = Course.courses.get(i);
            for (int j = 0; j < ln; j++) {
                if (i == j)
                    continue;
                Course c2 = Course.courses.get(j);
                int same = 0;
                for (IDict.Entry<String, Grade> grade: c1.grades) {
                    if (c2.grades.getIndexK(grade.getKey()) != -1)
                        same++;
                }
                if (same > c1.grades.getLength() / 2 && same > c2.grades.getLength() / 2)
                    exact[i].add(j);
            }
        }
        Set[] allR = new Set[ln];
        Set[] newR = new Set[ln];
        for (int i = 0; i < ln; i++) {
            allR[i] = new Set(-1, null);
            newR[i] = exact[i];
            Set newNewR = new Set(-1, null);
            int oldLength = -1;
            while (oldLength != allR[i].getLength()) {
                oldLength = allR[i].getLength();
                for (int j : newR[i]) {
                    if (j == -1)
                        continue;
                    if (j != i)
                        allR[i].add(Integer.parseInt(Course.courses.getKey(j)));
                    for (int k : exact[j])
                        newNewR.add(k);
                }
                newR[i] = newNewR;
            }
        }
        relationsC = allR;
    }

    private static void initialize2() {
        int ln = Course.courses.getLength();
        terms = new Set(-1, null);
        scores = new ArrayDict[ln];
        STerms = new Set[ln];
        for (int i = 0; i < ln; i++) {
            Course cr = Course.courses.get(i);
            for (IDict.Entry<String, Grade> grade: cr.grades) {
                terms.add(grade.getValue().term);
            }
        }
        for (int i = 0; i < ln; i++) {
            scores[i] = new ArrayDict<>();
            STerms[i] = new Set(-1, null);
            for (int t: terms) {
                scores[i].add(t, new Score());
                STerms[i].add(t);
            }
        }
        for (int i = 0; i < ln; i++) {
            Course cr = Course.courses.get(i);
            for (IDict.Entry<String, Grade> grade: cr.grades) {
                Grade g = grade.getValue();
                g.index = i;
                scores[i].getK(g.term).add(g.score);
            }
        }
    }

    private static void initialize3() {
        int ln = Student.students.getLength();
        Set[] exact = new Set[ln];
        for (int i = 0; i < ln; i++) {
            exact[i] = new Set(-1, null);
            Student s1 = Student.students.get(i);
            for (int j = 0; j < ln; j++) {
                if (i == j)
                    continue;
                Student s2 = Student.students.get(j);
                int same1 = 0;
                int same2 = 0;
                for (IDict.Entry<String, Grade> grade: s1.grades) {
                    Grade g = s2.grades.getK(grade.getKey());
                    if (g == null)
                        continue;
                    same2++;
                    if (grade.getValue().score > g.score)
                        same1++;
                }
                if (same1 > same2 / 2)
                    exact[i].add(j);
            }
        }
        Set[] allR = new Set[ln];
        Set[] newR = new Set[ln];
        for (int i = 0; i < ln; i++) {
            allR[i] = new Set(-1, null);
            newR[i] = exact[i];
            Set newNewR = new Set(-1, null);
            int oldLength = -1;
            while (oldLength != allR[i].getLength()) {
                oldLength = allR[i].getLength();
                for (int j : newR[i]) {
                    if (j == -1)
                        continue;
                    if (j != i)
                        allR[i].add(j);
                    for (int k : exact[j])
                        newNewR.add(k);
                }
                newR[i] = newNewR;
            }
        }
        relationsS = allR;
    }
}
