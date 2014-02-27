$('#submit').on('click', function()
{		
	var inputedTitle = $('#title').val();
	var inputedCode = $('#code').val();
	var inputedLanguage = $('#language').val();
	
	if(inputedTitle == "")
	{ 
		$('#title').css('border', '2px dashed red');
		return false;
	}
	else
	{
		$('#title').css('border', '1px solid #EEE');

	}
	if(inputedCode == "")
	{ 
		$('#code').css('border', '2px dashed red');
		return false;
	}
	else
	{
		$('#code').css('border', '1px solid #EEE');

	}
	if(inputedLanguage == "")
	{
		$('#language').css('border', '2px dashed red');
		return false;
	}
	else
	{
		$('#language').css('border', '1px solid #EEE');
	}
});

function passAndroidData() 
{
	var mTitle = $('#title').val();
    var mCode = $('#code').val();
    var mLang = $('#language').val();
    	
    var details = [mTitle,mCode,mLang];
    	
    Android.displayData(details);
};
function androidBack()
{
	Android.goBack();
};

$('#title').css('border', '1px solid #EEE');
$('#code').css('border', '1px solid #EEE');
$('#language').css('border', '1px solid #EEE'); 	