public class Grade {
    public final Student student;
    public final Course course;
    public final int term;
    public float score;
    public int index;

    public Grade(String studentID, String courseID, int term, float score) {
        this.student = Student.students.getK(studentID);
        this.course = Course.courses.getK(courseID);
        this.term = term;
        this.score = score;
        student.grades.add(courseID, this);
        course.grades.add(studentID, this);
    }

    public void deleteS() {
        student.grades.removeV(this);
    }

    public void deleteC() {
        course.grades.removeV(this);
    }

    @Override
    public String toString() {
        int intScore = (int) score;
        String s = term + " ";
        if (intScore == score)
            s += intScore;
        else
            s += score;
        return s;
    }
}
