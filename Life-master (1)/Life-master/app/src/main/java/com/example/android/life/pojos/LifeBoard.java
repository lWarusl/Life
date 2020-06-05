package com.example.android.life.pojos;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LifeBoard {

    public static final String GENDER_MALE = "Y";
    public static final String GENDER_FEMALE = "X";

    private String[][] mLifeBoard;
    private int mNoColumns;

    public LifeBoard(int noColumns){
        mNoColumns = noColumns;
        createNewBoard();
    }

    public void initBoard(int minStartPeople, int maxStartPeople){
        createNewBoard();
        int randomNoStartPeople = new Random().nextInt((maxStartPeople - minStartPeople) + 1) + minStartPeople;
        for(int i=0; i<randomNoStartPeople; i++){
            int randomX = new Random().nextInt(mNoColumns);
            int randomY = new Random().nextInt(mNoColumns);
            if(mLifeBoard[randomX][randomY]==null){
                if(new Random().nextBoolean()){
                    mLifeBoard[randomX][randomY] = GENDER_FEMALE;
                }else {
                    mLifeBoard[randomX][randomY] = GENDER_MALE;
                }
            }else {
                i--;
            }
        }
    }

    public void move(){
        String[][] updatedBoard = new String[mNoColumns][mNoColumns];
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++){
                updatedBoard[x][y] = mLifeBoard[x][y];
            }
        }

        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++){
                String gender = mLifeBoard[x][y];
                if(gender!=null){
                    List<int[]> possiblePositions = getPositionsOfFieldsAround(x, y);
                    Collections.shuffle(possiblePositions);

                    for(int[] position : possiblePositions){
                        int posX = position[0];
                        int posY = position[1];
                        if(posX==x && posY==y) {
                            break;
                        }else if(updatedBoard[posX][posY]==null && mLifeBoard[posX][posY]==null){
                            updatedBoard[posX][posY] = gender;
                            updatedBoard[x][y] = null;
                            break;
                        }
                    }
                }
            }
        }
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++){
                mLifeBoard[x][y] = updatedBoard[x][y];
            }
        }
    }

    public void kill(){
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++){
                String gender = mLifeBoard[x][y];
                if(gender!=null){
                    List<int[]> possiblePositions = getPositionsOfFieldsAround(x, y);

                    for(int[] position : possiblePositions){
                        int posX = position[0];
                        int posY = position[1];
                        String posGender = mLifeBoard[posX][posY];
                        if((posX!=x || posY!=y) && (posGender!=null)) {
                            if (posGender.equals(gender)) {
                                mLifeBoard[posX][posY] = null;
                                mLifeBoard[x][y] = null;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void reproduce(){
        int noChildren = 0;
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++){
                String gender = mLifeBoard[x][y];
                if(gender!=null){
                    List<int[]> possiblePositions = getPositionsOfFieldsAround(x, y);

                    for(int[] position : possiblePositions){
                        int posX = position[0];
                        int posY = position[1];
                        String posGender = mLifeBoard[posX][posY];
                        if((posX!=x || posY!=y) && (posGender!=null)){
                            if(!posGender.equals(gender)) {
                                noChildren++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        noChildren = noChildren / 2;
        for(int i=0; i<noChildren; i++){
            int randomX = new Random().nextInt(mNoColumns);
            int randomY = new Random().nextInt(mNoColumns);
            if(mLifeBoard[randomX][randomY]==null){
                if(new Random().nextBoolean()){
                    mLifeBoard[randomX][randomY] = GENDER_FEMALE;
                }else {
                    mLifeBoard[randomX][randomY] = GENDER_MALE;
                }
            }else {
                i--;
            }
        }


    }


    public boolean checkIfEndOfLife(){
        int countLives = countLives();
        int enoughPeople = ((mNoColumns*mNoColumns) * 3)/4;
        if(countLives<2 || countLives>=enoughPeople || countX()==0 || countY()==0){
            return true;
        }else {
            return false;
        }
    }

    public int countLives(){
        int countLives = 0;
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++) {
                if(mLifeBoard[x][y]!=null){
                    countLives++;
                }
            }
        }
        return countLives;
    }
    public int countX(){
        int countX = 0;
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++) {
                if(mLifeBoard[x][y]!=null){
                    if(mLifeBoard[x][y].equals(GENDER_FEMALE)){
                        countX++;
                    }
                }
            }
        }
        return countX;
    }

    public int countY(){
        int countY = 0;
        for(int x=0; x<mNoColumns; x++){
            for(int y=0; y<mNoColumns; y++) {
                if(mLifeBoard[x][y]!=null){
                    if(mLifeBoard[x][y].equals(GENDER_MALE)){
                        countY++;
                    }
                }

            }
        }
        return countY;
    }

    public List<PersonField> toList(){
        List<PersonField> people = new ArrayList<>();
        for(int i=0; i<mNoColumns*mNoColumns; i++){
            int x = i%mNoColumns;
            int y = i/mNoColumns;
            people.add(new PersonField(mLifeBoard[x][y]));
        }
        return people;
    }

    private void createNewBoard(){
        mLifeBoard = new String[mNoColumns][mNoColumns];
    }

    private List<int[]> getPositionsOfFieldsAround(int x, int y){
        List<int[]> positions = new ArrayList<>();
        int[] cor = new int[2];
        cor[0] = x; cor[1] = y;
        positions.add(cor);
        if(getPositionAbove(x, y)!=null) positions.add(getPositionAbove(x, y));
        if(getPositionDiagonallyUpRight(x, y)!=null) positions.add(getPositionDiagonallyUpRight(x, y));
        if(getPositionOnRight(x, y)!=null) positions.add(getPositionOnRight(x, y));
        if(getPositionDiagonallyUnderRight(x, y)!=null) positions.add(getPositionDiagonallyUnderRight(x, y));
        if(getPositionUnder(x, y)!=null) positions.add(getPositionUnder(x, y));
        if(getPositionDiagonallyUnderLeft(x, y)!=null) positions.add(getPositionDiagonallyUnderLeft(x, y));
        if(getPositionOnLeft(x, y)!=null) positions.add(getPositionOnLeft(x, y));
        if(getPositionDiagonallyUpLeft(x, y)!=null) positions.add(getPositionDiagonallyUpLeft(x, y));
        return positions;
    }

    private int[] getPositionDiagonallyUpRight(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldDiagonallyUpRight(x, y)){
            return null;
        }
        coOrdinates[0] = x+1;
        coOrdinates[1] = y-1;
        return coOrdinates;
    }

    private int[] getPositionDiagonallyUpLeft(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldDiagonallyUpLeft(x, y)){
            return null;
        }
        coOrdinates[0] = x-1;
        coOrdinates[1] = y-1;
        return coOrdinates;
    }

    private int[] getPositionDiagonallyUnderRight(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldDiagonallyUnderRight(x, y)){
            return null;
        }
        coOrdinates[0] = x+1;
        coOrdinates[1] = y+1;
        return coOrdinates;
    }

    private int[] getPositionDiagonallyUnderLeft(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldDiagonallyUnderLeft(x, y)){
            return null;
        }
        coOrdinates[0] = x-1;
        coOrdinates[1] = y+1;
        return coOrdinates;
    }

    private int[] getPositionOnLeft(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldOnLeft(x)){
            return null;
        }
        coOrdinates[0] = x-1;
        coOrdinates[1] = y;
        return coOrdinates;
    }

    private int[] getPositionOnRight(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldOnRight(x)){
            return null;
        }
        coOrdinates[0] = x+1;
        coOrdinates[1] = y;
        return coOrdinates;
    }

    private int[] getPositionAbove(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldAbove(y)){
            return null;
        }
        coOrdinates[0] = x;
        coOrdinates[1] = y-1;
        return coOrdinates;
    }

    private int[] getPositionUnder(int x, int y){
        int[] coOrdinates = new int[2];
        if(!isThereFieldUnder(y)){
            return null;
        }
        coOrdinates[0] = x;
        coOrdinates[1] = y+1;
        return coOrdinates;
    }

    private boolean isThereFieldDiagonallyUpRight(int x, int y){
        return (isThereFieldAbove(y)&&isThereFieldOnRight(x));
    }

    private boolean isThereFieldDiagonallyUpLeft(int x, int y){
        return (isThereFieldAbove(y)&&isThereFieldOnLeft(x));
    }

    private boolean isThereFieldDiagonallyUnderRight(int x, int y){
        return (isThereFieldUnder(y)&&isThereFieldOnRight(x));
    }

    private boolean isThereFieldDiagonallyUnderLeft(int x, int y){
        return (isThereFieldUnder(y)&&isThereFieldOnLeft(x));
    }

    private boolean isThereFieldOnLeft(int x){
        return x!=0;
    }

    private boolean isThereFieldOnRight(int x){
        return x!=(mNoColumns-1);
    }

    private boolean isThereFieldAbove(int y){
        return y!=0;
    }

    private boolean isThereFieldUnder(int y){
        return y!=(mNoColumns-1);
    }

    public void setLifeBoard(String[][] mLifeBoard) {
        this.mLifeBoard = mLifeBoard;
    }

    public void setNoColumns(int mNoColumns) {
        this.mNoColumns = mNoColumns;
    }

    public String[][] getLifeBoard() {
        return mLifeBoard;
    }

    public int getNoColumns() {
        return mNoColumns;
    }
}
