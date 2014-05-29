Patternizer CS349 A2
===========
A small program implemented using Java. 
It can be used to create different kinds of patterns.
Demo video provided by my prof: https://www.youtube.com/embed/qNdm6IZrbbg
My program behaves exactly the same as the demo video does.

1.   On the top of the screen, there are three radio buttons to select the thickness of the next polyline. Just click on the radio button to select.
     Mouse left button double click on the shape can randomly change the color of the shape. 
     Other functionalities are the same as the basic requirements.

2.   Clockwise is the positive direction of rotation. You can only rotate the shape within a range of 0 to 2pi.
     Double click on the circle will change the color of the circle as well. All colors are randomly generated. 
     According to the video, you have to click on the shape to select it. Just pressing down the mouse button will not select a shape.
     My code will adjust the position of the shape to the center of the circle during rotation if the shape's starting point is not the center of the circle.(To make the graph looks better. )

3. I added two enhancements in my code.
   The first one is to make it possible for user to select the thickness of the line. By clicking on different radio buttons, the thinkness of the next polyline to be drawn will be changed according to the selection. I used a JPanel with a GridLayout to hold the JRadioButtons and JLabels. And I also used a  ButtonGroup to ensure that only one radio button can be selected at anytime. I think my enhancement is worth full mark because: 1. It satisfy the requirement of the enhancement as described. 2. I used layout manager to make the layout of the toolbar and the shapes look better. 3. I made another enhancement as describe as below.

   The second enhancement that I added was direct manipulate to change a shape's colour. Double clicking on a shape will change a shape and all its instances' colour to a random colour. This is convenient for user to change the colour.

