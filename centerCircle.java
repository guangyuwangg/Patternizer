/*
 *  MyShape: See ShapeDemo2 for an example how to use this class.
 *
 */
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.*;

// simple shape class
class centerCircle {

	// shape model
	Point2d center;
	double D = 30; // diameter of the circle
	int whichColour = 0;
	Color color;

	public Color getColour() {
		return color;
	}

	public void setColour() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		color = randomColor;
	}

	public void setCenter(Point2d newCenter) {
		center = newCenter;
	}

	// paint the shape
	public void paint(Graphics2D g2) {
		g2.setColor(getColour());

		if (center != null) {
			g2.fillOval((int) Math.round(center.x - D/2),
					(int) Math.round(center.y - D/2), (int)D, (int)D);
		}
	}

	// return perpendicular vector
	static public Vector2d perp(Vector2d a) {
		return new Vector2d(-a.y, a.x);
	}

	// line-line intersection
	// return (NaN,NaN) if not intersection, otherwise returns intersecting
	// point
	static Point2d lineLineIntersection(Point2d P0, Point2d P1, Point2d Q0,
			Point2d Q1) {
		double x1 = P0.x;
		double y1 = P0.y;
		double x2 = P1.x;
		double y2 = P1.y;
		double x3 = Q0.x;
		double y3 = Q0.y;
		double x4 = Q1.x;
		double y4 = Q1.y;

		// check if two lines are
		if ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4) == 0) {
			return new Point2d(Double.NaN, Double.NaN);
		} else {
			double ix = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
					* (x3 * y4 - y3 * x4))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
			double iy = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
					* (x3 * y4 - y3 * x4))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
			return new Point2d(ix, iy);
		}
	}

	// affine transform helper
	// return P_prime = T * P
	Point2d transform(AffineTransform T, Point2d P) {
		Point2D.Double p = new Point2D.Double(P.x, P.y);
		Point2D.Double q = new Point2D.Double();
		T.transform(p, q);
		return new Point2d(q.x, q.y);

	}

	// hit test with this shape
	public boolean hittest(double x, double y) {
		// Idea: if the distance between the test point and the center is smaller than the radius, then the
		// point is within the shape.
		double distance = Math.round(Math.sqrt((x-center.x)*(x-center.x)+(y-center.y)*(y-center.y)));
		if (distance <= D/2){
			return true;
		}
		
		return false;
	}
}
