/*
 *  SimpleRenderer.java
 *  ARToolKit5
 *
 *  Disclaimer: IMPORTANT:  This Daqri software is supplied to you by Daqri
 *  LLC ("Daqri") in consideration of your agreement to the following
 *  terms, and your use, installation, modification or redistribution of
 *  this Daqri software constitutes acceptance of these terms.  If you do
 *  not agree with these terms, please do not use, install, modify or
 *  redistribute this Daqri software.
 *
 *  In consideration of your agreement to abide by the following terms, and
 *  subject to these terms, Daqri grants you a personal, non-exclusive
 *  license, under Daqri's copyrights in this original Daqri software (the
 *  "Daqri Software"), to use, reproduce, modify and redistribute the Daqri
 *  Software, with or without modifications, in source and/or binary forms;
 *  provided that if you redistribute the Daqri Software in its entirety and
 *  without modifications, you must retain this notice and the following
 *  text and disclaimers in all such redistributions of the Daqri Software.
 *  Neither the name, trademarks, service marks or logos of Daqri LLC may
 *  be used to endorse or promote products derived from the Daqri Software
 *  without specific prior written permission from Daqri.  Except as
 *  expressly stated in this notice, no other rights or licenses, express or
 *  implied, are granted by Daqri herein, including but not limited to any
 *  patent rights that may be infringed by your derivative works or by other
 *  works in which the Daqri Software may be incorporated.
 *
 *  The Daqri Software is provided by Daqri on an "AS IS" basis.  DAQRI
 *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE, REGARDING THE DAQRI SOFTWARE OR ITS USE AND
 *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 *  IN NO EVENT SHALL DAQRI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 *  MODIFICATION AND/OR DISTRIBUTION OF THE DAQRI SOFTWARE, HOWEVER CAUSED
 *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 *  STRICT LIABILITY OR OTHERWISE, EVEN IF DAQRI HAS BEEN ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package com.gestao.udec.gestao_mobile;

import android.content.Context;
import android.content.Intent;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import javax.microedition.khronos.opengles.GL10;


/**
 * A very simple Renderer that adds a marker and draws a cube on it.
 */
public class SimpleRenderer extends ARRenderer {

    private Context context;

	private int markerID = -1;
	private int markerID2= -1;
    private int markerID3= -1;
    private int markerID4= -1;
    private int markerID5= -1;
    private int markerID6= -1;
    private int markerID7= -1;
    private int markerID8= -1;
    private int markerID9= -1;
    private int markerID10= -1;

	//private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
	//private float angle = 0.0f;
	private boolean spinning = false;

    public SimpleRenderer(Context context){
        this.context = context;
    }

	@Override
	public boolean configureARScene() {

		markerID = ARToolKit.getInstance().addMarker("single;Data/1.pat;80");
        markerID2 = ARToolKit.getInstance().addMarker("single;Data/2.pat;80");
        markerID3 = ARToolKit.getInstance().addMarker("single;Data/3.pat;80");
        markerID4 = ARToolKit.getInstance().addMarker("single;Data/4.pat;80");
        markerID5 = ARToolKit.getInstance().addMarker("single;Data/5.pat;80");
        markerID6 = ARToolKit.getInstance().addMarker("single;Data/6.pat;80");
        markerID7 = ARToolKit.getInstance().addMarker("single;Data/7.pat;80");
        markerID8 = ARToolKit.getInstance().addMarker("single;Data/8.pat;80");
        markerID9 = ARToolKit.getInstance().addMarker("single;Data/9.pat;80");
        markerID10 = ARToolKit.getInstance().addMarker("single;Data/10.pat;80");
		if (markerID < 0 || markerID2<0 || markerID3<0 || markerID4<0 || markerID5<0 || markerID6<0 || markerID7<0 || markerID8<0 || markerID9<0 || markerID10<0) return false;

		return true;

	}

	public void click() {
		spinning = !spinning;
	}

	public void draw(GL10 gl) {

/*		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glFrontFace(GL10.GL_CW);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {

			gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);
			System.out.println("Kanji");
			gl.glPushMatrix();
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			cube.draw(gl);
			gl.glPopMatrix();

			if (spinning) angle += 5.0f;
		}
		*/
        Intent intent;
        intent = new Intent(context, EscanearActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


		if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
            intent.putExtra("aula", "1");
            context.startActivity(intent);
		}
        if (ARToolKit.getInstance().queryMarkerVisible(markerID2)) {
            intent.putExtra("aula", "2");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID3)) {
            intent.putExtra("aula", "3");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID4)) {
            intent.putExtra("aula", "4");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID5)) {
            intent.putExtra("aula", "5");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID6)) {
            intent.putExtra("aula", "6");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID7)) {
            intent.putExtra("aula", "7");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID8)) {
            intent.putExtra("aula", "8");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID9)) {
            intent.putExtra("aula", "9");
            context.startActivity(intent);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID10)) {
            intent.putExtra("aula", "10");
            context.startActivity(intent);
        }


	}
}