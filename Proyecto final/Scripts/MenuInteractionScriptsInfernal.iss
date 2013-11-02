; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{F9EF797D-EE27-493F-BD7A-237A6F178FEA}
AppName=MenuInteractions
AppVersion=1
;AppVerName=MenuInteractions 1
DefaultDirName={pf}\MenuInteractions
DisableDirPage=yes
DefaultGroupName=MenuInteractions
DisableProgramGroupPage=yes
OutputDir=D:\MenuInteracion
OutputBaseFilename=setup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Files]
Source: "D:\Repositorios\Assembla-ProyectoFinal\branches\Branch_Tesis_Sprint01\Dev\Interaction Module\MenuInteraction\MenuInteraction\bin\Debug\MenuInteraction.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Repositorios\Assembla-ProyectoFinal\branches\Branch_Tesis_Sprint01\Dev\Interaction Module\MenuInteraction\MenuInteraction\bin\Debug\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\MenuInteractions"; Filename: "{app}\MenuInteraction.exe"

