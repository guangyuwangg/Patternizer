/*
 *  MyShape: See ShapeDemo2 for an example how to use this class.
 *
 */
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.*;

// simple shape class
class MyShape {

	// shape model
	public ArrayList<Point2d> points;
	ArrayList<ArrayList<Point2d>> copies = new ArrayList<ArrayList<Point2d>>();
	Boolean isFilled = false; // shape is polyline or polygon
	Boolean isClosed = false; // polygon is filled or not
	Color colour = Color.BLACK;
	float strokeThickness = 3.0f;

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	public float getStrokeThickness() {
		return strokeThickness;
	}

	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public Boolean getIsFilled() {
		return isFilled;
	}

	public void setIsFilled(Boolean isFilled) {
		this.isFilled = isFilled;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	// for selection
	boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// for drawing
	Boolean hasChanged = false; // dirty bit if shape geometry changed
	int[] x_points, y_points;

	public float rotation = 0;
	public double scale = 1.0;

	// replace all points with array
	public void setPoints(double[][] pts) {
		points = new ArrayList<Point2d>();
		for (double[] p : pts) {
			points.add(new Point2d(p[0], p[1]));
		}
		hasChanged = true;
	}

	// add a point to end of shape
	public void addPoint(double x, double y) {
		if (points == null)
			points = new ArrayList<Point2d>();
		points.add(new Point2d(x, y));
		hasChanged = true;
	}

	// paint the shape
	public void paint(Graphics2D g2) {
		// update the shape in java Path2D object if it changed
		if (hasChanged) {
			x_points = new int[points.size()];
			y_points = new int[points.size()];
			for (int i = 0; i < points.size(); i++) {
				x_points[i] = (int) points.get(i).x;
				y_points[i] = (int) points.get(i).y;
			}
			hasChanged = false;
		}

		// don't draw if path2D is empty (not shape)
		if (x_points != null) {

			// special draw for selection
			if (isSelected) {
				g2.setColor(Color.YELLOW);
				g2.setStroke(new BasicStroke(strokeThickness * 4));
				if (isClosed)
					g2.drawPolygon(x_points, y_points, points.size());
				else
					g2.drawPolyline(x_points, y_points, points.size());
			}

			g2.setColor(colour);

			// call right drawing function
			if (isFilled) {
				g2.fillPolygon(x_points, y_points, points.size());
			} else {
				g2.setStroke(new BasicStroke(strokeThickness));
				if (isClosed)
					g2.drawPolygon(x_points, y_points, points.size());
				else
					g2.drawPolyline(x_points, y_points, points.size());
			}

			// draw copies
			if (copies.size() > 0) {
				for (ArrayList<Point2d> copy : copies) {
					int[] xPos, yPos;
					xPos = new int[copy.size()];
					yPos = new int[copy.size()];
					for (int i = 0; i < copy.size(); i++) {
						xPos[i] = (int) copy.get(i).x;
						yPos[i] = (int) copy.get(i).y;
					}
					g2.drawPolyline(xPos, yPos, copy.size());
				}
			}
		}
	}

	// find closest point
	static Point2d closestPoint(Point2d M, Point2d P1, Point2d P2) {
		// TODO: implement

		return new Point2d();
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

		// TODO: implement

		return new Point2d();
	}

	// affine transform helper
	// return P_prime = T * P
	Point2d transform(AffineTransform T, Point2d P) {
		Point2D.Double p = new Point2D.Double(P.x, P.y);
		Point2D.Double q = new Point2D.Double();
		T.transform(p, q);
		return new Point2d(q.x, q.y);

	}

	/**
	 * @param T
	 */
	public void rotate(double angle, double angle2, double scale1,
			double scale2, Point2d center) {
		double x;
		double y;
		if (points.size() > 0) {
			x = points.get(0).x;
			y = points.get(0).y;
			AffineTransform af = new AffineTransform();
			af.translate(center.x, center.y);
			if (angle2 > 0 && angle2 < 2 * Math.PI) {
				af.rotate(angle);
			}
			af.scale(scale2, scale2);
			af.scale(scale1, scale1);
			af.translate(-x, -y);
			for (int i = 0; i < points.size(); i++) {
				points.set(i, transform(af, points.get(i)));
			}
			if (angle2 > ((10 * Math.PI) / 360)) {
				changeCopies(angle2, center);
			} else {
				copies.clear();
			}
			hasChanged = true;
		}
	}

	// make circular array when rotate
	public void changeCopies(double angle, Point2d center) {
		copies.clear();
		angle = Math.abs(angle);
		double x;
		double y;
		for (double a = angle; a < 2 * Math.PI; a = a + angle) {
			ArrayList<Point2d> copy = new ArrayList<Point2d>();
			for (Point2d p : points) {
				copy.add(p);
			}
			x = copy.get(0).x;
			y = copy.get(0).y;
			AffineTransform af = new AffineTransform();
			af.translate(center.x, center.y);
			af.rotate(a);
			af.translate(-x, -y);
			for (int i = 0; i < copy.size(); i++) {
				copy.set(i, transform(af, copy.get(i)));
			}
			copies.add(copy);
		}
	}

	/**
	 * @param T
	 */
	public void move(AffineTransform T) {
		for (int i = 0; i < points.size(); i++) {
			points.set(i, transform(T, points.get(i)));
		}
		if(copies.size()>0){
			for(ArrayList<Point2d> copy: copies){
				for (int i = 0; i < copy.size(); i++) {
					copy.set(i, transform(T, copy.get(i)));
				}
			}
		}
		hasChanged = true;
	}

	// hit test with this shape
	public double hittest(double x, double y) {
		double distance = 10;
		if (points != null) {
			for (Point2d p : points) {
				double d = Math.sqrt((p.x - x) * (p.x - x) + (p.y - y)
						* (p.y - y));
				if (d < distance) {
					distance = d;
				}
			}
			if(copies.size()>0){
				for(ArrayList<Point2d> copy: copies){
					for (Point2d p : copy) {
						double d = Math.sqrt((p.x - x) * (p.x - x) + (p.y - y)
								* (p.y - y));
						if (d < distance) {
							distance = d;
						}
					}
				}
			}
			if (distance >= 10) {
				distance = -1;
			}
		}

		return distance;
	}
}
