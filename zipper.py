import zipfile
import os

# zip all java files in the current directory
def zip_files():
    with zipfile.ZipFile('b2220356127.zip', 'w', compression=zipfile.ZIP_STORED) as myzip:
        for file in os.listdir():
            # if file.endswith('.java'):
            #     myzip.write(file)
            if file == "Quiz4.java":
                myzip.write(file)

if __name__ == '__main__':
    zip_files()