#ifndef __JB_SCREENSHOT_H_
#define __JB_SCREENSHOT_H_

/**
 * screen shot
 * */
struct picture{
  int xres,yres;
  char *buffer;
  struct fb_cmap *colormap;
  char bps,gray;

  void free(){
	  if(colormap){
		  ::free(colormap->red);
		  ::free(colormap->blue);
		  ::free(colormap->green);
		  ::free(colormap->transp);
		  ::free(colormap);

		  colormap = NULL;
	  }

	  if(buffer){
		  ::free(buffer);
		  buffer = NULL;
	  }
  }
};

static const char* _display_device = "/dev/graphics/fb0";

int TakeScreenshot (const char* device, struct picture* pict);

#endif
