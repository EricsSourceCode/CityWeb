// This is adapted and modified from the original
// DefaultCaret.


// "This code is free software; you can redistribute
// it and/or modify it..."
// See the copyright and details below.




// OpenJDK
// src/share/classes/javax/swing/text/DefaultCaret.java

/*
 * Copyright (c) 1997, 2013, Oracle and/or its
 affiliates. All rights reserved.

 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS
 FILE HEADER.
 * This code is free software; you can redistribute
 it and/or modify it
 * under the terms of the GNU General Public
 License version 2 only, as
 * published by the Free Software Foundation.
  Oracle designates this
 * particular file as subject to the "Classpath"
 exception as provided
 * by Oracle in the LICENSE file that accompanied
 this code.
 * This code is distributed in the hope that
 it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty
 of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License
 * version 2 for more details (a copy is included
 in the LICENSE file that
 * accompanied this code).
 * You should have received a copy of the GNU
 General Public License version
 * 2 along with this work; if not, write to the
 Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston,
 MA 02110-1301 USA.
 * Please contact Oracle, 500 Oracle Parkway,
 Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional
 information or have any
 * questions.
 */




import javax.swing.text.DefaultCaret;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.plaf.TextUI;




public class CaretWide extends DefaultCaret
  {
  // It needs to have a version UID since it's
  // serializable.
  public static final long serialVersionUID = 3;
  private final int drawWidth = 8;



  public CaretWide()
    {
    }



  protected synchronized void damage( Rectangle r )
    {
    if( r == null )
      return;

    int damageWidth = drawWidth;
    x = r.x - 4 - (damageWidth >> 1);
    y = r.y;
    width = 9 + damageWidth;
    height = r.height;

    repaint();
    }




  public void paint( Graphics g )
    {
    if( !isVisible() )
      return;

    try
    {
    TextUI mapper = getComponent().getUI();
    Rectangle2D r = mapper.modelToView2D( getComponent(),
                                      getDot(),
                                      getDotBias() );

    if( r == null )
      return;

    Rectangle rectI = new Rectangle();
    rectI.x = (int)r.getX();
    rectI.y = (int)r.getY();
    rectI.width = (int)r.getWidth();
    rectI.height = (int)r.getHeight();

    if( (r.getWidth() <= 0) &&
                        (r.getHeight() <= 0) )
      return;


    if( !contains( r ) )
      {
      // It got out of sync and needs the right location?
      Rectangle clip = g.getClipBounds();
      if( clip != null )
        {
        if( !clip.contains( this ))
          {
          repaint();
          }
        }

      damage( rectI );
      }

    g.setColor( getComponent().getCaretColor() );
    rectI.x -= drawWidth  >> 1;
    g.fillRect( rectI.x, rectI.y,
                drawWidth, rectI.height );
    }
    catch( Exception e )
      {
      // mApp.snowStatus()
      }
    }



  }
