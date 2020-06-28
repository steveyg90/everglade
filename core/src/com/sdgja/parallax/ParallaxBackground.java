
//  ██████╗  █████╗ ██████╗  █████╗ ██╗     ██╗      █████╗ ██╗  ██╗██████╗  █████╗  ██████╗██╗  ██╗ ██████╗ ██████╗  ██████╗ ██╗   ██╗███╗   ██╗██████╗
//  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██║     ██║     ██╔══██╗╚██╗██╔╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝ ██╔══██╗██╔═══██╗██║   ██║████╗  ██║██╔══██╗
//  ██████╔╝███████║██████╔╝███████║██║     ██║     ███████║ ╚███╔╝ ██████╔╝███████║██║     █████╔╝ ██║  ███╗██████╔╝██║   ██║██║   ██║██╔██╗ ██║██║  ██║
//  ██╔═══╝ ██╔══██║██╔══██╗██╔══██║██║     ██║     ██╔══██║ ██╔██╗ ██╔══██╗██╔══██║██║     ██╔═██╗ ██║   ██║██╔══██╗██║   ██║██║   ██║██║╚██╗██║██║  ██║
//  ██║     ██║  ██║██║  ██║██║  ██║███████╗███████╗██║  ██║██╔╝ ██╗██████╔╝██║  ██║╚██████╗██║  ██╗╚██████╔╝██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝
//  ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝
//

package com.sdgja.parallax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.sdgja.map.IMap;

import static com.sdgja.intohell.IntoHellGame.*;

public class ParallaxBackground {
   
   private ParallaxLayer[] layers;
   private static Camera camera;
//   private SpriteBatch batch;
   private Vector2 speed = new Vector2();
   private float rgb = 0;
   private boolean cloudOn = true;
   
   /**
    * @param layers  The  background layers 
    * @param width   The screenWith 
    * @param height The screenHeight
    * @param speed A Vector2 attribute to point out the x and y speed
    */
   public ParallaxBackground(ParallaxLayer[] layers,float width,float height,Vector2 speed){
      this.layers = layers;
      this.speed.set(speed);
      camera = new OrthographicCamera(width, height);
      camera.position.add(getPlayer().getPlayerPosition().x,0, 0);
  //    batch = new SpriteBatch();

   }

   public boolean getCloudStatus() { return cloudOn; }
   public float getRGB() { return rgb; }

   public void setLayers(ParallaxLayer[] layers) {
      this.layers = layers;
   }

   public static void setWeatherCamera(){
      weatherBatch.setProjectionMatrix(camera.projection);
   }


   public void render(ShaderProgram p,float mainCamYPosition){
      cloudOn = true;
      rgb = 0;
      this.camera.position.add((this.speed.x * getCameraDiffernceX()) / 18, 0, 0);
      for(ParallaxLayer layer:layers){

//         weatherBatch.setProjectionMatrix(camera.projection);
//         if(p!=null)
//            weatherBatch.setShader(p);
//         else
            weatherBatch.setShader(null);
//         batch.begin();

         int topLimit = IMap.TOPFADELIMIT;
         int bottomLimit = IMap.BOTTOMFADELIMIT;
         if(mainCamYPosition < topLimit) {
            rgb = 1;
         } else if(mainCamYPosition < bottomLimit) {
                rgb = 1-((mainCamYPosition - topLimit) / (bottomLimit - topLimit));  //divisor;
         }
         if(rgb==0) {
            cloudOn = false;

         } else {

            float currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.getWidth() + layer.padding.x);

            if (speed.x < 0) currentX += -(layer.region.getWidth() + layer.padding.x);
            do {
               float currentY = -camera.position.y * layer.parallaxRatio.y % (layer.region.getHeight() + layer.padding.y);
               if (speed.y < 0) currentY += -(layer.region.getHeight() + layer.padding.y);
               do {
                  weatherBatch.draw(layer.region,
                          -this.camera.viewportWidth / 2 + currentX + layer.startPosition.x,
                          -this.camera.viewportHeight / 2 + currentY + layer.startPosition.y);
                  currentY += (layer.region.getHeight() + layer.padding.y);
               } while (currentY < camera.viewportHeight);
               currentX += (layer.region.getWidth() + layer.padding.x);
            } while (currentX < 1920);
         }
//         batch.end();
      }
   }
}
