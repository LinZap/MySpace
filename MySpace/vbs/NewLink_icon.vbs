Dim WshShell    ' 物件變數
Dim objArgs, Shortcut
Dim Arg(2),i

'設定使用者資料夾目錄的16進位碼
const UserProfile = &H28&

'建立WSH & Windows Shell物件
'------------------------------------------
set oWSHShell = CreateObject("Wscript.shell")
set oShell = CreateObject("Shell.Application")
Set WshShell = WScript.CreateObject("WScript.Shell")

'利用Shell物件取得桌面目錄路徑
'------------------------------------------
set oFolder = oShell.Namespace(UserProfile) 
set oFolderItem = oFolder.Self '取得FolderItem目錄
UserProfilePath = oFolderItem.Path '取得使用者資料夾路徑 '取得LINKS路徑

'設定捷徑相關資訊
'------------------------------------------
ShortCutName = "MySpace" '設定捷徑名稱
ShortCutPath = "C:\MySpace" '設定捷徑目錄，就是目標目錄

'建立捷徑
'------------------------------------------
set oShortCut = oWSHShell.CreateShortCut _
(UserProfilePath & "\Links\" & ShortCutName & ".lnk") '建立LINKS捷徑檔
oShortCut.TargetPath = ShortCutPath '設定該捷徑的目標
oShortCut.Save '儲存物件，這步驟才會真正建立捷徑---------------------------- 
Arg(0) = Arg(0) & oShortCut
Arg(1) = Arg(1) & "C:\MySpace\cat.ico"


' 試著取得參數
Set objArgs = WScript.Arguments    ' 建立物件

' 試著取得傳遞給此Script的參數
' 如果只有一個參數被傳遞過來，
' 則使用上面指定的預設圖示
For i = 0 to objArgs.Count - 1    ' 用來處理所有參數的迴圈
    Arg(i) = objArgs(i)           ' 取得參數
Next 

' 我們知道捷徑檔案和圖示位置的路徑
' 使用CreateShortcut方法來更新此捷徑
Set Shortcut = WshShell.CreateShortcut(Arg(0))

' 改變捷徑的圖示
Shortcut.IconLocation = Arg(1)

Shortcut.Save              ' 更新捷徑檔案
