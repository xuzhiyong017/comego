package com.duowan.fw.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.duowan.fw.root.BaseContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class JImageUtils {

	public static Bitmap decodeFile(String filePath) {
		return decodeFile(filePath, null);
	}

	public static Bitmap decodeByWidth(String filePath, int desiredWidth) {
		try {
			return decodeFileOrThrow(filePath, desiredWidth, 0);
		} catch (Throwable e) {
			return null;
		}
	}

	public static Rect decodeBmpSize(int resId) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(BaseContext.gContext.getResources(),
				resId, opts);
		return new Rect(0, 0, opts.outWidth, opts.outHeight);
	}

	public static Rect decodeBmpSize(String filePath) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		return new Rect(0, 0, opts.outWidth, opts.outHeight);
	}

	public static Bitmap decodeByHeight(String filePath, int desiredHeight) {
		try {
			return decodeFileOrThrow(filePath, 0, desiredHeight);
		} catch (Throwable e) {
			return null;
		}
	}

	public static Bitmap decodeByWidthOrThrow(String filePath, int desiredWidth) {
		return decodeFileOrThrow(filePath, desiredWidth, 0);
	}

	public static Bitmap decodeByHeightOrThrow(String filePath,
			int desiredHeight) {
		return decodeFileOrThrow(filePath, 0, desiredHeight);
	}

	/**
	 * Decode file with given options. Will prefer use a smaller sample size to
	 * save memory, If this is not up to demand, use the one with more
	 * parameter: {@link #decodeFileOrThrow(String, int, int, boolean)}. NOTE
	 * OutOfMemoryError will be eaten here, and null returned in this case.
	 * 
	 * @param filePath
	 *            File path.
	 * @param desiredWidth
	 *            Desired width, can be 0. If set to 0, desiredHeight will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @param desiredHeight
	 *            Desired height, can be 0. If set to 0, desiredWidth will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @return Bitmap decoded, or null if failed.
	 */
	public static Bitmap decodeFile(String filePath, int desiredWidth,
			int desiredHeight) {
		try {
			return decodeFileOrThrow(filePath, desiredWidth, desiredHeight);
		} catch (Throwable e) {
			JLog.warn("decoeFile", "fail to decode %s, %s", filePath,
					e.toString());
			return null;
		}
	}

	/**
	 * Decode file with given options. Will prefer use a smaller sample size to
	 * save memory, If this is not up to demand, use the one with more
	 * parameter: {@link #decodeFileOrThrow(String, int, int, boolean)}. NOTE
	 * OutOfMemoryError will be eaten here, and null returned in this case.
	 * 
	 * @param desiredWidth
	 *            Desired width, can be 0. If set to 0, desiredHeight will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @param desiredHeight
	 *            Desired height, can be 0. If set to 0, desiredWidth will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @return Bitmap decoded, or null if failed.
	 */
	public static Bitmap decodeResource(int resId, int desiredWidth,
			int desiredHeight) {
		if (desiredWidth <= 0 && desiredHeight <= 0) {
			return decodeResource(resId);
		}
		try {
			return decodeResOrThrow(resId, desiredWidth, desiredHeight, true);
		} catch (Throwable e) {
			JLog.error("YYImageUtils", e);
			return null;
		}
	}

	public static Bitmap decodeResource(int resId) {
		try {
			return BitmapFactory.decodeResource(
					BaseContext.gContext.getResources(), resId);
		} catch (java.lang.OutOfMemoryError e) {
			JLog.error("YYImageUtils", e);
		}
		return null;
	}

	public static void decodeResBounds(int resId, Options decodeOps) {
		decodeOps.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeResource(
					BaseContext.gContext.getResources(), resId);
		} catch (java.lang.OutOfMemoryError e) {
		}
	}

	/**
	 * Decode file with given options. Will prefer use a smaller sample size to
	 * save memory, If this is not up to demand, use the one with more
	 * parameter: {@link #decodeFileOrThrow(String, int, int, boolean)}. NOTE
	 * OutOfMemoryError can be throw here.
	 * 
	 * @param filePath
	 *            File path.
	 * @param desiredWidth
	 *            Desired width, can be 0. If set to 0, desiredHeight will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @param desiredHeight
	 *            Desired height, can be 0. If set to 0, desiredWidth will be
	 *            honored. If both desiredWidth and desiredHeight are 0, the
	 *            original bitmap will be decoded.
	 * @return Bitmap decoded, or null if failed.
	 */
	public static Bitmap decodeFileOrThrow(String filePath, int desiredWidth,
			int desiredHeight) {
		return decodeFileOrThrow(filePath, desiredWidth, desiredHeight, true);
	}

	/**
	 * Decode file with given options. NOTE OutOfMemoryError can be throw here.
	 * 
	 * @param filePath
	 *            File path.
	 * @param desiredWidth
	 *            Desired width, can be 0. If set to 0, maximum width will be
	 *            used, i.e. : desiredHeight will take effect. If both
	 *            desiredWidth and desiredHeight are 0, the original bitmap will
	 *            be decoded.
	 * @param desiredHeight
	 *            Desired height, can be 0. If set to 0, maximum height will be
	 *            used. i.e. : desiredWidth will take effect. If both
	 *            desiredWidth and desiredHeight are 0, the original bitmap will
	 *            be decoded.
	 * @param isMemoryPrior
	 *            If true, will prefer to use a bigger sample size to use less
	 *            memory, otherwise prefer to use a smaller sample size, the the
	 *            returned bitmap can be with bigger size, and can be probably
	 *            more vivid.
	 * @return Bitmap decoded, or null if failed.
	 */
	public static Bitmap decodeFileOrThrow(String filePath, int desiredWidth,
			int desiredHeight, boolean isMemoryPrior) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		if (opts.outWidth <= 0 || opts.outHeight <= 0) {
			return null;
		}

		int sampleSize = calSampleSize(desiredWidth, desiredHeight,
				isMemoryPrior, opts);

		opts.inJustDecodeBounds = false;
		opts.inSampleSize = sampleSize;
		return BitmapFactory.decodeFile(filePath, opts);
	}

	/**
	 * Decode file with given options. NOTE OutOfMemoryError can be throw here.
	 * 
	 * @param desiredWidth
	 *            Desired width, can be 0. If set to 0, maximum width will be
	 *            used, i.e. : desiredHeight will take effect. If both
	 *            desiredWidth and desiredHeight are 0, the original bitmap will
	 *            be decoded.
	 * @param desiredHeight
	 *            Desired height, can be 0. If set to 0, maximum height will be
	 *            used. i.e. : desiredWidth will take effect. If both
	 *            desiredWidth and desiredHeight are 0, the original bitmap will
	 *            be decoded.
	 * @param isMemoryPrior
	 *            If true, will prefer to use a bigger sample size to use less
	 *            memory, otherwise prefer to use a smaller sample size, the the
	 *            returned bitmap can be with bigger size, and can be probably
	 *            more vivid.
	 * @return Bitmap decoded, or null if failed.
	 */
	public static Bitmap decodeResOrThrow(int drawableId, int desiredWidth,
			int desiredHeight, boolean isMemoryPrior) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;

		final Resources res = BaseContext.gContext.getResources();
		BitmapFactory.decodeResource(res, drawableId, opts);
		if (opts.outWidth <= 0 || opts.outHeight <= 0) {
			return null;
		}

		int sampleSize = calSampleSize(desiredWidth, desiredHeight,
				isMemoryPrior, opts);

		opts.inJustDecodeBounds = false;
		opts.inSampleSize = sampleSize;
		return BitmapFactory.decodeResource(res, drawableId, opts);
	}

	private static int calSampleSize(int desiredWidth, int desiredHeight,
			boolean isMemoryPrior, Options opts) {
		int sampleSize = 1;
		if (desiredWidth == 0 && desiredHeight == 0) {
			sampleSize = 1;
		} else if (desiredHeight == 0) {
			sampleSize = (opts.outWidth + desiredWidth - 1) / desiredWidth;
		} else if (desiredWidth == 0) {
			sampleSize = (opts.outHeight + desiredHeight - 1) / desiredHeight;
		} else {
			final int horRatio = (opts.outWidth + desiredWidth - 1)
					/ desiredWidth;
			final int verRatio = (opts.outHeight + desiredHeight - 1)
					/ desiredHeight;
			sampleSize = isMemoryPrior ? Math.max(horRatio, verRatio) : Math
					.min(horRatio, verRatio);
		}
		return sampleSize;
	}

	public static Bitmap decodeFile(String filePath, BitmapFactory.Options opt) {
		if (JStringUtils.isNullOrEmpty(filePath)) {
			return null;
		}
		Bitmap bmp = null;
		try {
			File file = new File(filePath);
			if (file.isFile()) {
				bmp = BitmapFactory.decodeFile(filePath, opt);
			} else {
				JLog.error(JImageUtils.class, filePath + " is not a file");
			}
		} catch (OutOfMemoryError err) {
			JLog.error(JImageUtils.class, "oom: " + filePath);
			bmp = null;
		}
		return bmp;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int maxBorderLength,
			boolean recycle) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newHeight = 0;
		int newWidth = 0;
		if (width > height) {
			float ratio = ((float) height) / ((float) width);
			newWidth = maxBorderLength;
			newHeight = (int) ((newWidth) * ratio);
		} else if (height > width) {
			float ratio = ((float) width) / ((float) height);
			newHeight = maxBorderLength;
			newWidth = (int) ((newHeight) * ratio);
		} else {
			newWidth = maxBorderLength;
			newHeight = maxBorderLength;
		}
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, true);
			return resizedBitmap;
		} catch (java.lang.OutOfMemoryError e) {
		}
		return null;
	}

	public static void resizeImage(String imageFile, String newFileName,
			int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = 0;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageFile, options);
		if (options.outWidth <= 0 || options.outHeight <= 0) {
			JLog.error(JImageUtils.class, "bitmap width or height is zero");
			return;
		}
		options.inJustDecodeBounds = false;

		int max = options.outWidth;
		int min = options.outHeight;
		if (options.outWidth < options.outHeight) {
			max = options.outHeight;
			min = options.outWidth;
		}
		int sampleSize = 1;
		int nextMax = max >> 1;
		int nextMin = min >> 1;

		// width is supposed to be bigger in general, but if it is not, just
		// reverse them
		if (maxWidth < maxHeight) {
			int temp = maxWidth;
			maxWidth = maxHeight;
			maxHeight = temp;
		}
		while (nextMax >= maxWidth && nextMin >= maxHeight) {
			sampleSize <<= 1;
			nextMax >>= 1;
			nextMin >>= 1;
		}
		options.inSampleSize = sampleSize;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(imageFile, options);
		} catch (java.lang.OutOfMemoryError e) {
		}
		if (bitmap != null) {
			try {
				JFileUtils out = JFileUtils.openFile(newFileName);
				out.write(bitmap, JConstant.IMAGE_COMPRESS_RATE);
				out.close();
			} catch (Exception e) {
				JLog.error(JImageUtils.class, e);
			}
		}
	}

    /**
     * 调整Bitmap大小
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap resizeBmp(Bitmap bitmap, int maxWidth, int maxHeight, boolean releaseSource) {
        if (bitmap == null){
            return null;
        }
        if (bitmap.getWidth() == maxWidth && bitmap.getHeight() == maxHeight) {
            return bitmap;
        }
        Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true);
        if (releaseSource) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return resizedBmp;
    }

	public static int getRotate(String filepath) {
		try {
			ExifInterface exif = new ExifInterface(filepath);
			return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getAngleFromRotateEnum(int rotate) {
		switch (rotate) {
		case ExifInterface.ORIENTATION_ROTATE_180:
			return 180;
		case ExifInterface.ORIENTATION_ROTATE_90:
			return 90;
		case ExifInterface.ORIENTATION_ROTATE_270:
			return 270;
		default:
			return 0;
		}
	}

	public static int createThumbnail(String filename) {
		if (!JImageUtils.isImage(filename)) {
			return 0;
		}
		String thumbnailName = JFileUtils.getRootThumbImageLocalPath(filename);
		if (!JFileUtils.isFileExisted(thumbnailName)) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filename, options);
			if (options.outHeight <= 0 || options.outWidth <= 0) {
				JLog.error(JImageUtils.class,
						"bitmap width or height is zero when create thumbnail");
				return 0;
			}
			int longBorder = Math.max(options.outHeight, options.outWidth);
			options.inJustDecodeBounds = false;
			options.inSampleSize = longBorder / JConstant.THUMBNAIL_WIDTH;
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
				if (bitmap != null) {
					saveBitmapToFile(bitmap, thumbnailName);
				} else {
					return 0;
				}
			} catch (Exception e) {
				JLog.error("Utils.createThumbnail", e);
				return -1;
			} catch (java.lang.OutOfMemoryError e) {
				JLog.error("Utils.createThumbnail", e);
				return -1;
			}
		}
		return 1;
	}

	// resize bitmap if it's size exceeded maxWidth or maxHeight. and return the
	// passed in bitmap if
	// no need to change size
	public static Bitmap rotateAndResizeImage(Bitmap inBitmap, int maxWidth,
			int maxHeight, int rotate) {
		int imgWidth = inBitmap.getWidth();
		int imgHeight = inBitmap.getHeight();
		boolean needResize = imgWidth > maxWidth || imgHeight > maxHeight;
		boolean needRotate = getAngleFromRotateEnum(rotate) != 0;
		if (needResize || needRotate) {
			Matrix matrix = new Matrix();
			if (needResize) {
				float scale = Math.min(maxWidth / (float) imgWidth, maxHeight
						/ (float) imgHeight);
				matrix.postScale(scale, scale);
			}
			if (needRotate) {
				matrix.postRotate(getAngleFromRotateEnum(rotate));
			}
			try {
				Bitmap resultBitmap = Bitmap.createBitmap(inBitmap, 0, 0,
						imgWidth, imgHeight, matrix, true);
				return resultBitmap;
			} catch (java.lang.OutOfMemoryError e) {
			}
		}
		return inBitmap;
	}

	public static Bitmap decodeImageFromStream(InputStream queryStream,
			InputStream decodeStream) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = 0;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(queryStream, null, options);

		if (options.outWidth <= 0 || options.outHeight <= 0) {
			JLog.error(JImageUtils.class, "bitmap width or height is zero");
			return null;
		}
		options.inJustDecodeBounds = false;
		int widthScale = options.outWidth / JConstant.IMAGE_SCALE_WIDTH;
		int heightScale = options.outHeight / JConstant.IMAGE_SCALE_HEIGHT;
		options.inSampleSize = widthScale > heightScale ? widthScale
				: heightScale;
		options.inScaled = false;

		try {
			return BitmapFactory.decodeStream(decodeStream, null, options);
		} catch (java.lang.OutOfMemoryError e) {
			JLog.error("YYImageUtils", "decodeImageFromStream error, OOM");
		}
		return null;
	}

	public static Bitmap decodeBmpFromRes(int resId) {
		try {
			return BitmapFactory.decodeResource(
					BaseContext.gContext.getResources(), resId);
		} catch (java.lang.OutOfMemoryError e) {
			JLog.error("YYImageUtils", "decodeBmpFromRes error, OOM");
		}
		return null;
	}

	public static void saveBitmapToFile(Bitmap bitmap, String filename)
			throws Exception {
		if (bitmap != null && filename != null) {
			JFileUtils out = JFileUtils.openFile(filename);
			out.write(bitmap, JConstant.IMAGE_COMPRESS_RATE);
			out.close();
		}
	}

	public static boolean isImage(InputStream queryStream) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = 0;
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(queryStream, null, options);
			return (options.outWidth > 0 && options.outHeight > 0);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isImage(String imageFile) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outHeight = 0;
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(imageFile, options);
			return (options.outWidth > 0 && options.outHeight > 0);
		} catch (Exception e) {
			return false;
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xffffffff;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	private static Bitmap sDefaultMalePhoto = null;
	private static Bitmap sDefaultMalePhotoOffline = null;
	private static Bitmap sDefaultFemalePhotoOffline = null;
	private static Bitmap sDefaultMalePhotoBitmap = null;
	private static Bitmap sDefaultFemalePhotoBitmap = null;
	private static Drawable sAddFriendIcon = null;

	public static Drawable getAddFriendIcon() {
		if (sAddFriendIcon == null) {
			// sAddFriendIcon =
			// YYMobile.gContext.getResources().getDrawable(R.drawable.icon_find_friend);
		}
		return sAddFriendIcon;
	}

	public static boolean isNotDefaultPortrait(Bitmap image) {
		return (image != sDefaultFemalePhotoBitmap
				&& image != sDefaultMalePhotoBitmap
				&& image != sDefaultMalePhotoOffline
				&& image != sDefaultFemalePhotoOffline
				&& image != sDefaultMalePhoto && image != sDefaultMalePhotoOffline);
	}

	public static Bitmap getUserPortraitBmp(Context c, String photoPath,
			int gender, boolean online) {
		return getUserPortraitBmp(c, photoPath, gender, online,
				JConstant.PhotoWidth);
	}

	public static Bitmap getUserPortraitBmp(Context c, String photoPath,
			int gender, boolean online, int preferWidth) {
		if (!JStringUtils.isNullOrEmpty(photoPath)) {
			try {
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(photoPath);
				if (preferWidth == 0) {
					option.inSampleSize = 1;
				} else {
					option.inSampleSize = option.outWidth / preferWidth;
				}
				option.inJustDecodeBounds = false;
				Bitmap bmp = BitmapFactory.decodeFile(photoPath, option);
				if (bmp != null) {
					if (online) {
						return bmp;
					} else {
						Bitmap grayBmp = getGrayBmp(bmp);
						bmp.recycle();
						return grayBmp;
					}
				}
			} catch (Exception e) {
			} catch (java.lang.OutOfMemoryError e) {
			}
		}
		return getDefaultPhoto(c, gender, online);
	}

	public static Bitmap getGrayBmp(final Bitmap image) {
		if (image != null) {
			try {
				Bitmap grayscalBitmap = Bitmap.createBitmap(image.getWidth(),
						image.getHeight(), Config.RGB_565);
				Canvas canvas = new Canvas(grayscalBitmap);
				Paint paint = new Paint();
				ColorMatrix matrix = new ColorMatrix();
				matrix.setSaturation(0);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
						matrix);
				paint.setColorFilter(filter);
				canvas.drawBitmap(image, 0, 0, paint);
				return grayscalBitmap;
			} catch (Exception e) {
				JLog.error("Utils.getGrayBmp", e);
			} catch (java.lang.OutOfMemoryError e) {
				JLog.error("Utils.getGrayBmp", e);
			}
		}
		return null;
	}

	public static Bitmap getDefaultPhoto(Context c, int gender, boolean online) {
		Bitmap bmp = getDefaultPhotoBitmap(c, gender);
		if (!online) {
			if (gender == JModelConstant.Gender.FEMALE) {
				if (sDefaultFemalePhotoOffline == null) {
					sDefaultFemalePhotoOffline = getGrayBmp(bmp);
				}
				return sDefaultFemalePhotoOffline;
			} else if (gender == JModelConstant.Gender.MALE) {
				if (sDefaultMalePhotoOffline == null) {
					sDefaultMalePhotoOffline = getGrayBmp(bmp);
				}
				return sDefaultMalePhotoOffline;
			}
		}
		return bmp;
	}

	public static Bitmap getDefaultPhotoBitmap(Context c, int gender) {
		if (gender == JModelConstant.Gender.FEMALE) {
			if (sDefaultFemalePhotoBitmap == null) {
				// sDefaultFemalePhotoBitmap =
				// decodeResource(R.drawable.female_yy_bear);
			}
			return sDefaultFemalePhotoBitmap;
		} else {
			if (sDefaultMalePhotoBitmap == null) {
				// sDefaultMalePhotoBitmap =
				// decodeResource(R.drawable.male_yy_bear);
			}
			return sDefaultMalePhotoBitmap;
		}
	}

	public static void resizeWideImageToSquare(String imagePath) {
		if (!JStringUtils.isNullOrEmpty(imagePath)) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.outHeight = 0;
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imagePath, options);
				if (options.outWidth <= 0 || options.outHeight <= 0) {
					JLog.error(JImageUtils.class, "imagePath = " + imagePath
							+ ", bitmap width or height is zero");
					return;
				}
				if (options.outWidth != options.outHeight) {
					int newWidth = Math
							.min(options.outWidth, options.outHeight);
					Rect rect = new Rect();
					if (options.outWidth > options.outHeight) {
						rect.top = 0;
						rect.bottom = newWidth;
						rect.left = (options.outWidth - newWidth) / 2;
						rect.right = rect.left + newWidth;
					} else {
						rect.left = 0;
						rect.right = newWidth;
						rect.top = (options.outHeight - newWidth) / 2;
						rect.bottom = rect.top + newWidth;
					}
					Bitmap bmp = BitmapFactory.decodeFile(imagePath);
					Bitmap newBmp = createClipBitmap(bmp, rect);
					try {
						JFileUtils.removeFile(imagePath);
						JFileUtils out = JFileUtils.createImageFile(JFileUtils
								.getFileName(imagePath));
						out.write(newBmp);
						out.close();
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
			} catch (java.lang.OutOfMemoryError e) {
			}
		}
	}

	public static Bitmap createClipBitmap(Bitmap bmp, Rect photoRect) {
		// the right and bottom must be checked for their values are converted
		// from float math to integer value and might be bigger than actual
		// bitmap because of round error
		Bitmap portrait = null;
		try {
			if (bmp != null) {
				int bmpWidth = bmp.getWidth();
				int bmpHeight = bmp.getHeight();
				if (bmpWidth > 0 && bmpHeight > 0) {
					photoRect.right = photoRect.right > bmpWidth ? bmpWidth
							: photoRect.right;
					photoRect.bottom = photoRect.bottom > bmpHeight ? bmpHeight
							: photoRect.bottom;
					portrait = Bitmap.createBitmap(bmp, photoRect.left,
							photoRect.top, photoRect.width(),
							photoRect.height());
					if (bmp != portrait && !bmp.isRecycled()) // old bitmap is
																// useless, free
																// memory
						bmp.recycle();
				}
			}
		} catch (Exception e) {
		}
		return portrait;
	}

	public static Bitmap decodeResource(int resId, Options opt) {
		try {
			return BitmapFactory.decodeResource(
					BaseContext.gContext.getResources(), resId, opt);
		} catch (java.lang.OutOfMemoryError e) {
			JLog.error("lcy", e);
		}
		return null;
	}

	public static interface PORTRAIT_OPS {
		public static final int SMALL = 0;
		public static final int BIG = 1;
		public static final int ORIGINAL = 2;
	}

	public static void setUserIcon(ImageView view, String path, int gender) {
		setUserIcon(view, path, gender, JModelConstant.UserStatus.USER_ONLINE,
				true, PORTRAIT_OPS.BIG);
	}

	private static void setUserIcon(ImageView view, String path, int gender,
			int status, boolean alwaysOnline, int ops) {
		Bitmap img = null;
		boolean online = (alwaysOnline || status == JModelConstant.UserStatus.USER_ONLINE);
		switch (ops) {
		case PORTRAIT_OPS.ORIGINAL:
			img = getUserPortraitBmp(view.getContext(), path, gender, online, 0);
			break;
		case PORTRAIT_OPS.BIG:
			img = getUserPortraitBmp(view.getContext(), path, gender, online);
			break;
		default:
			// img = SimpleBitmapCache.instance().getUserPhoto(path, gender,
			// online);
			break;
		}
		view.setImageBitmap(img);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

    public static Uri addImageToAlbum(Context context, String filePath){
        // Read back the compressed file size.
        File f = new File(filePath);
        if(!f.exists()){
            return null;
        }
        String filename = f.getName();
        long size = f.length();
        try{
        ContentValues values = new ContentValues(9);
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, "Gaga");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.SIZE, size);
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
}
