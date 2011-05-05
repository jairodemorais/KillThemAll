package org.games.killthemall;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private static final int BMP_COLLUMNS = 3;
	private static final int BMP_ROWS = 4;
	private static final int[] DIRECTION_TO_ANIMATON_MAP = {3,1,0,2};
	private int x;
	private int y;
	private int xSpeed;
	private int ySpeed;
	private GameView gameView;
	private Bitmap bmp;
	private int width;
	private int height;
	private int currentFrame;
	

	public Sprite (GameView gameView, Bitmap bmp){
		this.gameView = gameView;
		this.bmp = bmp;
		this.width = bmp.getWidth() / BMP_COLLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		Random rnd = new Random();
		xSpeed = rnd.nextInt(10)-5;
		ySpeed = rnd.nextInt(10)-5;
		x = rnd.nextInt(gameView.getWidth()- width);
		y = rnd.nextInt(gameView.getHeight()-height);
	}
	
	private void update(){
		if(x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0){
			xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		if(y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0){
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;
		currentFrame = ++currentFrame % BMP_COLLUMNS;
	}
	
	public void onDraw (Canvas canvas){
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect src = new Rect(srcX,srcY, srcX + width, srcY + height);
		Rect des = new Rect(x,y , x+ width, y + height);
		canvas.drawBitmap(bmp, src, des, null);
	}
	//direction = 0 up, 1 left, 2 down, 3 right,
	//animation = 3 back, 1 left, 0 front, 2 right
	private int getAnimationRow(){
		double dirDouble = (Math.atan2(xSpeed, ySpeed)/(Math.PI /2)+2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		return DIRECTION_TO_ANIMATON_MAP[direction];
	}

	public boolean isCollision(float x2, float y2) {
		return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
	}
}
