package evolution.util;

import java.util.Objects;

public class Vector2 {
    public int x;
    public int y;


    public Vector2 add(Vector2 v) {
        this.x += v.x; this.y += v.y;
        return this;
    }


    public Vector2 sub(Vector2 v) {
        this.x -= v.x; this.y -= v.y;
        return this;
    }


    public Vector2 mult(double scalar) {
        this.x *= scalar; this.y *= scalar;
        return this;
    }


    public double distanceTo(Vector2 v) {
        return Math.sqrt((this.x-v.x)*(this.x-v.x) + (this.y-v.y)*(this.y-v.y));
    }


    public int distanceMTo(Vector2 v) {
        return Math.abs(this.x - v.x) + Math.abs(this.y - v.y);
    }


    public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }


    public int lengthM() {
        return Math.abs(this.x) + Math.abs(this.y);
    }


    public Vector2 clamp(Vector2 min, Vector2 max) {
        if (this.x < min.x)         this.x = min.x;
        else if (this.x > max.x)    this.x = max.x;
        if (this.y < min.y)         this.y = min.y;
        else if (this.y > max.y)    this.y = max.y;
        return this;
    }


    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }

    // overrides

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2 v)) return false;
        return x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // constructors

    public Vector2() {
        this.x = 0; this.y = 0;
    }

    public Vector2(int x, int y) {
        this.x = x; this.y = y;
    }

    // pseudo-constructors

    static public Vector2 ZERO() {
        return new Vector2(0,0);
    }

    static public Vector2 UP() {
        return new Vector2(0,-1);
    }

    static public Vector2 UPRIGHT() {
        return new Vector2(1,-1);
    }

    static public Vector2 RIGHT() {
        return new Vector2(1,0);
    }

    static public Vector2 DOWNRIGHT() {
        return new Vector2(1,1);
    }

    static public Vector2 DOWN() {
        return new Vector2(0,1);
    }

    static public Vector2 DOWNLEFT() {
        return new Vector2(-1,1);
    }

    static public Vector2 LEFT() {
        return new Vector2(-1,0);
    }

    static public Vector2 UPLEFT() {
        return new Vector2(-1,-1);
    }
}
