package evolution.util;

import java.util.Objects;

/**
 * Class implementing basic vector in 2d space with various useful methods.
 */
public class Vector2 {
    public int x;
    public int y;

    /**
     * Adds two vectors together in-place.
     * @param v vector that will be added to this one
     * @return itself
     */
    public Vector2 add(Vector2 v) {
        this.x += v.x; this.y += v.y;
        return this;
    }

    /**
     * Subtracts vector from this one in-place.
     * @param v vector that will be subtracted
     * @return itself
     */
    public Vector2 sub(Vector2 v) {
        this.x -= v.x; this.y -= v.y;
        return this;
    }

    /**
     * Multiplies vector by a scalar in-place.
     * @param scalar value to be multiplied by
     * @return itself
     */
    public Vector2 mult(double scalar) {
        this.x *= scalar; this.y *= scalar;
        return this;
    }

    /**
     * Calculates distance to the other point in 2d space using euclidean distance formula.
     * @param v other point in space
     * @return calculated distance
     */
    public double distanceTo(Vector2 v) {
        return Math.sqrt((this.x-v.x)*(this.x-v.x) + (this.y-v.y)*(this.y-v.y));
    }


    /**
     * Calculates distance to the other point in 2d space using manhattan distance formula.
     * @param v other point in space
     * @return calculated distance
     */
    public int distanceMTo(Vector2 v) {
        return Math.abs(this.x - v.x) + Math.abs(this.y - v.y);
    }


    /**
     * Returns own length using euclidean distance formula.
     * @return calculated length
     */
    public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    /**
     * Returns own length using manhattan distance formula.
     * @return calculated length
     */
    public int lengthM() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    /**
     * Clamps both vector's values by appropriate vector's "min" value and vector's "max" value. Works in-place.
     * @return itself
     */
    public Vector2 clamp(Vector2 min, Vector2 max) {
        if (this.x < min.x)         this.x = min.x;
        else if (this.x > max.x)    this.x = max.x;
        if (this.y < min.y)         this.y = min.y;
        else if (this.y > max.y)    this.y = max.y;
        return this;
    }

    /**
     * Create exact copy of this object.
     * @return new copied object
     */
    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }

    // overrides

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }

    // constructors

    public Vector2() {
        this.x = 0; this.y = 0;
    }


    public Vector2(int x, int y) {
        this.x = x; this.y = y;
    }


    public Vector2(Vector2 v) {
        this.x = v.x; this.y = v.y;
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

    // hash & equals

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
}
