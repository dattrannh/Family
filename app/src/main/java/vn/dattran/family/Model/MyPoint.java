package vn.dattran.family.Model;

/**
 * Created by DatTran on 09/02/2018.
 */

public class MyPoint {
    private float x;
    private float y;
    private float dx;
    private float dy;
    private Person person;

    public MyPoint() {
    }

    public MyPoint(float x, float y, float dx, float dy, Person person) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.person = person;
    }

    public float getX() {
        return x;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
