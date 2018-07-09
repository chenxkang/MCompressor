# MCompressor
一个简单的图片压缩工具，可自定义压缩的格式和质量，以及压缩后存储的文件路径，可决定对多大的文件进行压缩。

# 使用方法

## build.gradle文件

### Step 1. Add the JitPack repository to your build file

#### Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Step 2. Add the dependency

	dependencies {
    	 implementation 'com.github.chenxkang:MCompressor:1.0.2'
    }


## 示例

### 最简单的使用方法，只需要添加原文件路径即可

    MCompressor.from()
               .fromFilePath(path)
               .compress();

### 对大于500KB的文件进行压缩，压缩的文件格式为JPEG，压缩质量为80%，原文件路径为path，压缩文件保存路径可自定义

    MCompressor.from()
               .compressFormat(Bitmap.CompressFormat.JPEG)
               .quality(80)
               .greaterThan(500, CompressUnit.KB)
               .fromFilePath(path)
               .toFilePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
               .compress();
