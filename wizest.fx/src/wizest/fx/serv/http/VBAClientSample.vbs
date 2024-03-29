Public Declare Function SetTimer Lib "user32" ( _
    ByVal HWnd As Long, ByVal nIDEvent As Long, _
    ByVal uElapse As Long, ByVal lpTimerFunc As Long) As Long
    
Public Declare Function KillTimer Lib "user32" ( _
    ByVal HWnd As Long, ByVal nIDEvent As Long) As Long
    
Public TimerID As Long

Sub StartTimer()
    TimerID = SetTimer(0&, 0&, 1000&, AddressOf Send)
    '타이머 설정. 1000&는 약 1초
End Sub

Sub StopTimer()
     KillTimer 0&, TimerID
    '타이머 해제
End Sub

Sub Send()
    'Text = SendText(Now() & " helloooooooooooooooo~")
    Text = SendText(GetTextAsCSV())
    
    Debug.Print Text
End Sub

Function SendText(Text)
    On Error GoTo ErrHandler:

    'Debug.Print "Start Time: " & Time
    'Debug.Print Text
    
    Dim WinHTTP As Object
    Dim ResponseText
    Dim BodySent
    
    BodySent = Text
    Set WinHTTP = CreateObject("WinHttp.WinHttpRequest.5.1")
    'Set WinHTTP = New WinHttpRequest
    
    WinHTTP.Open "PUT", "http://127.0.0.1:8888", False
    WinHTTP.SetTimeouts 1000, 1000, 1000, 1000
    
    WinHTTP.setRequestHeader "Content-Type", "text/plain"
    
    WinHTTP.Send BodySent
    
    'Debug.Print WinHTTP.Status & " - " & WinHTTP.StatusText
    If WinHTTP.StatusText = "OK" Then
        ResponseText = WinHTTP.ResponseText
        'Debug.Print ResponseText
    End If
    
    Set WinHTTP = Nothing
    
    SendText = ResponseText
    'Debug.Print "End Time: " & Time
    
    Exit Function
    
ErrHandler:
    SendText = Date & " " & Time & ":" & Error
    Debug.Print SendText
    
End Function

Public Function GetTextAsCSV()
    On Error GoTo Err
    Dim b As Workbook
    Dim s As Worksheet
    
    For bi = 1 To Workbooks.Count
        Set b = Workbooks(bi)
        'Debug.Print b.Name
        For si = 1 To b.Sheets.Count
            Set s = b.Sheets(si)
            'Debug.Print s.Name
            Dim ur As Range
            Set ur = s.UsedRange
            Dim csv As String
            csv = ""
            Dim i, j As Integer
            For i = 1 To ur.Rows.Count
                For j = 1 To ur.Columns.Count
                    csv = csv & ur(i, j).Text & ","
                Next
                    csv = csv & vbNewLine
            Next i
            'Debug.Print csv
            GetTextAsCSV = GetTextAsCSV & csv
        Next si
    Next bi
    'Debug.Print GetUsedRangeAsCSVString
    Exit Function
Err:
    GetTextAsCSV = ""
End Function


