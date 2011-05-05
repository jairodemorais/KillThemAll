package org.games.killthemall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private Bitmap bmp;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<TempSprite> temps = new ArrayList<TempSprite>();
	private long lastClick;
	private Bitmap bmpBlood;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
				createSprites();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {

			}
		});
		
		bmpBlood =  BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
	}
	
	private void createSprites()
	{
		sprites.add(createSprite(R.drawable.bad1));
		sprites.add(createSprite(R.drawable.bad2));
		sprites.add(createSprite(R.drawable.bad3));
		sprites.add(createSprite(R.drawable.bad4));
		sprites.add(createSprite(R.drawable.bad5));
		sprites.add(createSprite(R.drawable.bad6));
		sprites.add(createSprite(R.drawable.good1));
		sprites.add(createSprite(R.drawable.good2));
		sprites.add(createSprite(R.drawable.good3));
		sprites.add(createSprite(R.drawable.good4));
		sprites.add(createSprite(R.drawable.good5));
		sprites.add(createSprite(R.drawable.good6));
	}

	private Sprite createSprite(int resource){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
		return new Sprite(this,bmp);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(System.currentTimeMillis()-lastClick > 500){
			lastClick = System.currentTimeMillis();
		//avoid execute at the same time the draw and the remove. make the synchronized 
		synchronized (getHolder()){
		float y = event.getY();
		float x = event.getX();
		//loop since last element to the first because otherwise it will crash.
		for(int i = sprites.size() -1; i>= 0; i--){
			Sprite sprite = sprites.get(i);
			if(sprite.isCollision(x, y)){
				sprites.remove(sprite);
				temps.add(new TempSprite(temps, this, x, y, bmpBlood));
				break;
			}
		}}};
		return super.onTouchEvent(event);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		for(int i = temps.size()-1;i>=0;i--){
			temps.get(i).onDraw(canvas);
		}
		for(Sprite sprite : sprites){
			sprite.onDraw(canvas);
		}
	}
}
