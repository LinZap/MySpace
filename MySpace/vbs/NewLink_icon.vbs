Dim WshShell    ' �����ܼ�
Dim objArgs, Shortcut
Dim Arg(2),i

'�]�w�ϥΪ̸�Ƨ��ؿ���16�i��X
const UserProfile = &H28&

'�إ�WSH & Windows Shell����
'------------------------------------------
set oWSHShell = CreateObject("Wscript.shell")
set oShell = CreateObject("Shell.Application")
Set WshShell = WScript.CreateObject("WScript.Shell")

'�Q��Shell������o�ୱ�ؿ����|
'------------------------------------------
set oFolder = oShell.Namespace(UserProfile) 
set oFolderItem = oFolder.Self '���oFolderItem�ؿ�
UserProfilePath = oFolderItem.Path '���o�ϥΪ̸�Ƨ����| '���oLINKS���|

'�]�w���|������T
'------------------------------------------
ShortCutName = "MySpace" '�]�w���|�W��
ShortCutPath = "C:\MySpace" '�]�w���|�ؿ��A�N�O�ؼХؿ�

'�إ߱��|
'------------------------------------------
set oShortCut = oWSHShell.CreateShortCut _
(UserProfilePath & "\Links\" & ShortCutName & ".lnk") '�إ�LINKS���|��
oShortCut.TargetPath = ShortCutPath '�]�w�ӱ��|���ؼ�
oShortCut.Save '�x�s����A�o�B�J�~�|�u���إ߱��|---------------------------- 
Arg(0) = Arg(0) & oShortCut
Arg(1) = Arg(1) & "C:\MySpace\cat.ico"


' �յۨ��o�Ѽ�
Set objArgs = WScript.Arguments    ' �إߪ���

' �յۨ��o�ǻ�����Script���Ѽ�
' �p�G�u���@�ӰѼƳQ�ǻ��L�ӡA
' �h�ϥΤW�����w���w�]�ϥ�
For i = 0 to objArgs.Count - 1    ' �ΨӳB�z�Ҧ��Ѽƪ��j��
    Arg(i) = objArgs(i)           ' ���o�Ѽ�
Next 

' �ڭ̪��D���|�ɮשM�ϥܦ�m�����|
' �ϥ�CreateShortcut��k�ӧ�s�����|
Set Shortcut = WshShell.CreateShortcut(Arg(0))

' ���ܱ��|���ϥ�
Shortcut.IconLocation = Arg(1)

Shortcut.Save              ' ��s���|�ɮ�
