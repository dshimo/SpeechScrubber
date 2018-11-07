$(document).ready(function() {
    var upload_url = "";
    var timestamp_map;
    $.ajax({
        url: upload_url
    }).done(function(data) {
        if(data){
            // Display results
        }
        else {
            // Display error
        }
    });

    $('#file_browse').on('change', function(event){
        // Insert the uploaded audio file in the audio portion.
        $('#audio').removeClass('hidden');
        
        var audio = document.getElementById('audio');
        var reader = new FileReader();
        reader.onload = function(event) {
            audio.src = this.result;
            audio.controls = true;
            // audio.play();
        };
        reader.readAsDataURL(this.files[0]);
    });

    // Upon clicking the file upload icon, click the hidden file input to open up a finder.
    $('#file_upload').on('click', function(event){
        event.stopPropagation();
        $('#file_browse').click();
    });

    // Handle clicking on part of the transcript and jumping to the respective spot in the audio clip.
    $('#transcript span').on('click', function(){
        var span = this;
        console.log("Clicked on '" + span.innerText + "' in the transcript.");

        $('#audio')[0].currentTime = 5;
    });
});