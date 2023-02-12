public class Course implements INameID<String, String, Course> {
    public static final IDict<String, Course> courses;

    static {
        if (Main.p == 1)
            courses = new ArrayDict<>();
        else
            courses = new FastHashDict<>(Main.a, Main.b, Main.p);
    }

    public final ArrayDict<String, Grade> grades = new ArrayDict<>();
    public final String ID;
    private String name;
    public int hash;
    private Point<Course> point;

    public Course(String ID, String name) {
        this.ID = ID;
        this.name = name;
        courses.add(ID, this);
    }

    private Course() {
        this.ID = null;
        this.name = "";
    }

    public void delete() {
        for (IDict.Entry<String, Grade> grade: grades) {
            grade.getValue().deleteS();
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
            ((FastHashDict<Course>) courses).tree.add(this);
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
    public Point<Course> getPoint() {
        return point;
    }

    @Override
    public void setPoint(Point<Course> p) {
        point = p;
    }

    @Override
    public Course createNull() {
        return new Course();
    }

    public void print() {
        System.out.println(ID + " " + name + " " + grades.getLength());
        for (IDict.Entry<String, Grade> grade: grades)
            System.out.println(grade.getKey() + " " + grade.getValue().toString());
    }
}
