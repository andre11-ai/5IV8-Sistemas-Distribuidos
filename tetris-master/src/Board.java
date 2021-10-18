import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import sun.misc.LRUCache;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Studio
 */
public class Board extends JPanel implements ActionListener {

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (canMoveTo(currentShape, filaact, colmact - 1)) {
                        colmact--;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (canMoveTo(currentShape, filaact, colmact + 1)) {
                        colmact++;
                    }
                    break;
                case KeyEvent.VK_UP:
                    Shape rotShape = currentShape.rotateRight();
                    if (canMoveTo(rotShape, filaact, colmact)) {
                        currentShape = rotShape;
                    }

                    break;
                case KeyEvent.VK_DOWN:
                    if (canMoveTo(currentShape, filaact + 1, colmact)) {
                        filaact++;
                    }
                    break;

                case KeyEvent.VK_P:
                    if (!timer.isRunning()) {
                        score.resume();
                        timer.start();
                    } else {
                        timer.stop();
                        score.pause();
                    }
                    break;

                default:
                    break;
            }
            repaint();
        }

    }

    private JFrame parentFrame;

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public static final int filas = 22;
    public static final int columnas = 10;

    private Tetrominoes[][] matrix;
    private int deltaTime;

    private Shape currentShape;

    private int colmact;
    private int filaact;

    private Timer timer;

    private MyKeyAdapter myKeyAdepter;
    private ScoreBoard score;

    public final int ironda = -2;

    private boolean paused = false;

    public NextShape nextShape;

    public void setScoreBoard(ScoreBoard score) {
        this.score = score;
    }

    public void setNextShape(NextShape nextShape) {
        this.nextShape = nextShape;
    }

    public Board() {
        super();

        matrix = new Tetrominoes[filas][columnas];

        initValues();
        timer = new Timer(deltaTime, this);
        myKeyAdepter = new MyKeyAdapter();

    }

    private void initValues() {
        setFocusable(true);

        cleanBoard();

        deltaTime = 500;
        currentShape = null;

        filaact = ironda;
        colmact = columnas / 2;

    }

    public void initGame() {

        initValues();
        timer.start();
        score.reset();

        //currentShape = new Shape(); // = Shape.getRandomShape()
        currentShape = Shape.getRandomShape();

        removeKeyListener(myKeyAdepter);
        addKeyListener(myKeyAdepter);

    }

    private void cleanBoard() {
        for (int row = 0; row < filas; row++) {
            for (int col = 0; col < columnas; col++) {

                matrix[row][col] = Tetrominoes.NoShape;
            }
        }
    }

    private boolean canMoveTo(Shape shape, int newRow, int newCol) {
        if ((newCol + shape.getXmin() < 0)
                || (newCol + shape.getXmax() >= columnas)
                || (newRow + shape.getYmax() >= filas)
                || hitWithMatrix(shape, newRow, newCol)) {
            return false;
        }
        return true;
    }

    private boolean hitWithMatrix(Shape shape, int newRow, int newCol) {
        int[][] squaresArray = shape.getCoordinates();

        for (int point = 0; point <= 3; point++) {
            int row = newRow + squaresArray[point][1];
            int col = newCol + squaresArray[point][0];
            if (row >= 0) { //para que no se salga del indice de matrix (INIT_ROW = -2)
                if (matrix[row][col] != Tetrominoes.NoShape) {
                    return true;
                }
            }
        }
        return false;
    }

    //Main Game loop
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (canMoveTo(currentShape, filaact + 1, colmact)) {
            filaact++;

        } else {
            if (GO()) {
                gO();
            } else {
                moveCurrentShapeToMatrix();
                checkRow();

                //currentShape = new Shape();
                currentShape = nextShape.getShape();
                nextShape.generateNewShape();

                filaact = ironda;
                colmact = columnas / 2;

            }

        }
        repaint(); //no se puede llamar directamente a paintComponent
    }

    public boolean GO() {

        int[][] squaresArray = currentShape.getCoordinates();
        for (int point = 0; point <= 3; point++) {
            int row = filaact + squaresArray[point][1];
            //int col = currentCol + squaresArray[point][0];

            if (row < 0) {
                return true;
            }
        }
        return false;
    }

    public void gO() {
        score.setText("GAME OVER");

        removeKeyListener(myKeyAdepter);
        timer.stop();

        GameOver dialog = new GameOver((JFrame) getParent().getParent().getParent().getParent(), true, score);
        dialog.setVisible(true);

        RecordsDialog d = new RecordsDialog(parentFrame, true, score.getScore());
        d.setVisible(true);

    }

    private void checkRow() {
        boolean lineNoWhite = true;

        for (int i = 0; i < filas; i++) {
            lineNoWhite = true;
            for (int j = 0; j < columnas; j++) {
                if (matrix[i][j] == Tetrominoes.NoShape) {
                    lineNoWhite = false;
                }
            }
            if (lineNoWhite) {
                cleanRow(i);

                if (deltaTime >= 100) {
                    if (score.getScore() % 5 == 0) {
                        deltaTime -= 50;
                        timer.setDelay(deltaTime);
                    }
                }

            }
        }
    }

    private void cleanRow(int numRow) {
        for (int i = numRow; i > 0; i--) {
            for (int j = 0; j < columnas; j++) {
                matrix[i][j] = matrix[i - 1][j];
            }
        }
        for (int j = 0; j < columnas; j++) {
            matrix[0][j] = Tetrominoes.NoShape;
        }

        score.increment(1);

    }

    private void moveCurrentShapeToMatrix() {
        int[][] squaresArray = currentShape.getCoordinates();
        for (int point = 0; point <= 3; point++) {
            int row = filaact + squaresArray[point][1];
            int col = colmact + squaresArray[point][0];
            matrix[row][col] = currentShape.getShape();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (currentShape != null) {
            currentShape.draw(g, filaact, colmact, squareWidth(), squareHeight());
        }
        drawBorder(g);

    }

    public void drawBorder(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(0, 0, columnas * squareWidth(), filas * squareHeight());
    }

    public void drawBoard(Graphics g) {
        for (int row = 0; row < filas; row++) {
            for (int col = 0; col < columnas; col++) {

                Util.drawSquare(g, row, col, matrix[row][col], squareWidth(), squareHeight());
            }
        }
    }

    private int squareWidth() {
        return getWidth() / columnas;
    }

    private int squareHeight() {
        return getHeight() / filas;
    }

}
