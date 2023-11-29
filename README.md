# Wago SmartScript for Linux

### Install dependencies
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
# Commands used in this readme
sudo apt-get install jq curl git
```

### Download innoextract
Make a new directory:
```bash
mkdir SmartScript
cd SmartScript
mkdir downloads
cd downloads
```
Download and (extract into it) `innoextract` from [this repo](https://github.com/dscharrer/innoextract/releases/latest).

You need version >= 1.9, because an outdated version from apt-get will not work.

### Download (and extract) SmartScript

**TODO:** Make it work with the newest version.

Now it works with [4.9.0.3](https://smartdata.wago.com/smartscript/update/smartScript_4.9.0.3.zip).

<s>

```bash
curl `curl https://smartdata.wago.com/smartscript/versioninfo/ | jq --raw-output '"\(.installerFolderUrl)/smartScript_\(.newestVersion).zip"'` -O -J
unzip smartScript_*.zip
```
</s>

```bash
curl https://smartdata.wago.com/smartscript/update/smartScript_4.9.0.3.zip -O -J
unzip smartScript_*.zip
```

Now use innoextract to extract the executable.
```bash
cd ..
./downloads/innoextract ./downloads/*.exe
```

### Download external libs
Download `Byte Buddy` files:
* [Byte Buddy](https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy) to `app/app/` folder.
* [Byte Buddy Agent](https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy-agent) to `app/` folder.

and [`JavaFX`](https://gluonhq.com/products/javafx/) SDK, and extract it into downloads.

### Apply the patch
Clone this repository (into SmartScript)
```bash
git clone https://github.com/Aar0nTh3Hack3r/SmartScript4Linux.git
cd SmartScript4Linux
```
Build the project
```bash
make
```

### Executecute
```bash
./SmartScript.sh
```
Enjoy!