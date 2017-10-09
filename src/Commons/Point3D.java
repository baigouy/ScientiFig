package Commons;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Point3D
 *
 * @author Benoit Aigouy
 */
public abstract class Point3D implements java.io.Serializable {

    private static final long serialVersionUID = -5276960640259649960L;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    public static class Integer extends Point3D implements java.io.Serializable{

        private static final long serialVersionUID = -5276960640259649961L;

        public int x;
        public int y;
        public int z;

        /**
         * Creates a Point3D out of a point and a z coordinate
         *
         * @param pt
         * @param z
         * @since <B>Packing Analyzer 3.0</B>
         */
        public Integer(Point pt, int z) {
            this(pt.x, pt.y, z);
        }

        /**
         * Creates a Point3D (0,0,0)
         *
         * @since <B>Packing Analyzer 3.0</B>
         */
        public Integer() {
        }

        /**
         * Creates a Point3D
         *
         * @param x
         * @param y
         * @param z
         * @since <B>Packing Analyzer 3.0</B>
         */
        public Integer(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            super.x = x;
            super.y = y;
            super.z = z;
        }

        public Point getPoint() {
            return new Point(x, y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public void setX(int x) {
            this.x = x;
            super.x = x;
        }

        public void setY(int y) {
            this.y = y;
            super.y = y;
        }

        public void setZ(int z) {
            this.z = z;
            super.z = z;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point3D) {
            Point3D tmp = (Point3D) obj;
            return this.x == tmp.x && this.y == tmp.y && this.z == tmp.z;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[x=" + x + ", y=" + y + ", z=" + z + "]";
    }

    public static class Double extends Point3D implements java.io.Serializable{

        private static final long serialVersionUID = -5276960640259649962L;

        public Double() {
        }

        public Double(Point2D.Double pt, double z) {
            this(pt.x, pt.y, z);
        }

        public Double(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point getPoint() {
            return new Point((int) Math.round(x), (int) Math.round(y));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setZ(double z) {
            this.z = z;
        }

    }
}
