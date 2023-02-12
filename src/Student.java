public class Student implements INameID<String, String, Student> {
    public static final IDict<String, Student> students;

    static {
        if (Main.p == 1)
            students = new ArrayDict<>();
        else
            students = new FastHashDict<>(Main.a, Main.b, Main.p);
    }

    public final ArrayDict<String, Grade> grades = new ArrayDict<>();
    public final String ID;
    private String name;
    public int hash;
    private Point<Student> point;

    public Student(String ID, String name) {
        this.ID = ID;
        this.name = name;
        students.add(ID, this);
    }

    private Student() {
        this.ID = null;
        this.name = "";
    }

    public void delete() {
        for (IDict.Entry<String, Grade> grade: grades) {
            grade.getValue().deleteC();
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (Main.p != 1) {
            point.delete(null);
            ((FastHashDict<Student>) students).tree.add(this);
        }
    }

    @Override
    public void hashCode(int hash) {
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public Point<Student> getPoint() {
        return point;
    }

    @Override
    public void setPoint(Point<Student> p) {
        point = p;
    }

    @Override
    public Student createNull() {
        return new Student();
    }

    public void print() {
        System.out.println(ID + " " + name + " " + grades.getLength());
        for (IDict.Entry<String, Grade> grade: grades)
            System.out.println(grade.getKey() + " " + grade.getValue().toString());
    }
}
