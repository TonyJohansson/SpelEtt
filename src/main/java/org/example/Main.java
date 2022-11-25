package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.googlecode.lanterna.TextColor.ANSI.*;


public class Main  {
    static DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
    static Terminal terminal;

    static {
        try {
            terminal = terminalFactory.createTerminal();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception  {

        int x = 20;
        int y = 20;
        int speed = 25;
        int hundratal = '1';
        int tiotal = '1';
        int ental = '1';
        char pten = (char)tiotal;
        char point = (char)ental;
        char phun = (char)hundratal;
        final char snakeHead = '\u2662';
        final char target = '\u2766' ;  // ☃♦
        final char block = '\u2588';
        KeyStroke latestKeyStroke = null;
        boolean continueReadingInput = true;
        String s = "SCORE 000";
        String lose = "GAME OVER";
        String creator = "Creators:";
        String names = "Mia, Martin, Tony";
        String love = "Du är fin idag";
        List<String> printLove = List.of(love.split(" "));
        terminal.setCursorVisible(false);
        Random r = new Random();
        List<Position> wall = new ArrayList<>();
        List<Position> snake = new ArrayList<>(List.of(new Position(x, y)));
        Position tarPos = new Position(r.nextInt(16,78), r.nextInt(1,22));

        printWall(wall,block);
        terminal.setForegroundColor(GREEN);
        printScore(s);
        terminal.setForegroundColor(YELLOW);
        printLove(printLove);
        terminal.setCursorPosition(tarPos.x, tarPos.y);
        terminal.putCharacter(target);
        terminal.setForegroundColor(RED);
        printSnake(snake, snakeHead);

        while (continueReadingInput) {
            int index = 0;
            KeyStroke keyStroke = null;
            do {
                index++;
                if (index % speed == 0) {
                    if (latestKeyStroke != null) {  // Autowalk
                        KeyType lType = latestKeyStroke.getKeyType();
                        switch (lType) {
                            case ArrowDown -> y += 1;
                            case ArrowUp -> y -= 1;
                            case ArrowLeft -> x -= 1;
                            case ArrowRight -> x += 1;
                        }
                        if (wall.contains(new Position(x, y))) { // crashinto wall = death
                            continueReadingInput = false;
                            printGameOver(lose,creator,names);
                            System.out.println("Death");
                            latestKeyStroke = null;
                        } else if (snake.contains(new Position(x,y))){ // crashinto self = death
                            continueReadingInput = false;
                            printGameOver(lose,creator,names);
                            System.out.println("Death");
                        } else if (snake.contains(tarPos)) { // Eats, grows and Point+1
                            snake.add(0,new Position(x,y));
                            setPutSnakehead(snake, snakeHead);
                            tarPos = new Position(r.nextInt(16,78), r.nextInt(1,22));
                            terminal.setForegroundColor(YELLOW);
                            terminal.setCursorPosition(tarPos.x,tarPos.y);
                            terminal.putCharacter(target);
                            terminal.setForegroundColor(GREEN);
                            terminal.setCursorPosition(9,1);
                            terminal.putCharacter(point);
                            terminal.setForegroundColor(RED);
                            if ((char)ental == '9') {
                                ental = '/';
                            }
                            if (ental == '0') {
                                if(speed > 10){
                                    speed-=5;
                                }
                                if ((char)tiotal == '9') {
                                    tiotal = '/';
                                }
                                terminal.setCursorPosition(8,1);
                                terminal.setForegroundColor(GREEN);
                                terminal.putCharacter(pten);
                                terminal.setForegroundColor(RED);
                                tiotal++;
                                pten = (char) tiotal;
                                if (tiotal == '1') {
                                    terminal.setCursorPosition(7,1);
                                    terminal.setForegroundColor(GREEN);
                                    terminal.putCharacter(phun);
                                    terminal.setForegroundColor(RED);
                                    hundratal++;
                                    phun = (char) hundratal;
                                }
                            }
                            ental++;
                            point = (char)ental;
                        } else { // Take step (add head, remove tail)
                            snake.add(0,new Position(x,y));
                            setPutSnakehead(snake, snakeHead);
                            terminal.setCursorPosition(snake.get(snake.size()-1));
                            terminal.putCharacter(' ');
                            snake.remove(snake.size()-1);
                        }
                        terminal.flush();
                    }
                }
                Thread.sleep(5);
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);
            latestKeyStroke = keyStroke;
            Character c = keyStroke.getCharacter();


            if (c == Character.valueOf('q')) { //Quit the game
                continueReadingInput = false;
                System.out.println("QUIT");
                terminal.close();
            }
            terminal.flush();
        }

    }

    private static void printLove(List<String> printnames) throws IOException {
        for (int i = 0; i < printnames.get(0).length(); i++) {
            terminal.setCursorPosition(i+4, 19);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(printnames.get(0).charAt(i));
        }
        for (int i = 0; i < printnames.get(1).length(); i++) {
            terminal.setCursorPosition(i+4, 20);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(printnames.get(1).charAt(i));
        }
        for (int i = 0; i < printnames.get(2).length(); i++) {
            terminal.setCursorPosition(i+4, 21);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(printnames.get(2).charAt(i));
        }
        for (int i = 0; i < printnames.get(3).length(); i++) {
            terminal.setCursorPosition(i+4, 22);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(printnames.get(3).charAt(i));
        }
    }

    private static void printGameOver(String lose, String creator,String names) throws IOException {
        for (int i = 0; i < lose.length(); i++) {
            terminal.setCursorPosition(i+40, 10);
            terminal.setForegroundColor(RED);
            terminal.putCharacter(lose.charAt(i));
        }
        for (int i = 0; i < creator.length(); i++) {
            terminal.setCursorPosition(i+40, 12);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(creator.charAt(i));
        }
        for (int i = 0; i < names.length(); i++) {
            terminal.setCursorPosition(i+36, 14);
            terminal.setForegroundColor(YELLOW);
            terminal.putCharacter(names.charAt(i));
        }
    }

    private static void setPutSnakehead(List<Position> snake, char snakeHead) throws IOException {
        terminal.setCursorPosition(snake.get(0));
        terminal.putCharacter(snakeHead);
    }

    private static void printScore(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            terminal.setCursorPosition(i+1, 1);
            terminal.putCharacter(s.charAt(i));
        }
    }

    private static void printSnake(List<Position> snake, char snakeHead) throws IOException {
        for (Position pos: snake) {
            terminal.setCursorPosition(pos);
            terminal.putCharacter(snakeHead);
        }
    }
    public static void printWall(List<Position> wall, final char block) throws IOException {
        for (int i = 0; i < 24; i++) {
            wall.add(new Position(11,i));
            wall.add(new Position(0,i));//vänster
        }
        for (int i = 0; i < 79; i++) {
            wall.add(new Position(79,i)); //höger
        }
        for (int i = 0; i < 79; i++) {
            wall.add(new Position(i,0));// över
        }
        for (int i = 0; i < 79; i++) {
            wall.add(new Position(i,23)); // under
        }
        for (Position walla: wall) {
            terminal.setCursorPosition(walla);
            terminal.putCharacter(block);
        }
    }

}
