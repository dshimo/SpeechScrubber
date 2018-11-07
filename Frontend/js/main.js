$(document).ready(function() {
    var upload_url = "";
    var timestamp_map; // Map of words to timestamp

    var uploadFile = function(file){
        $.ajax({
            url: upload_url,
            type: "POST",
            data: file,
            success: function(data){
                if(data){
                    // Transcript comes back
                }
            },
            error: function(response){
                // Display error
            }
        });
    };    

    $('#file_browse').on('change', function(){
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
        uploadFile(this.files[0]);
    });

    // Upon clicking the file upload icon, click the hidden file input to open up a finder.
    $('#file_upload').on('click', function(event){
        event.stopPropagation();
        $('#file_browse').click();
    });

    // Handle clicking on part of the transcript and jumping to the respective spot in the audio clip.
    $('#transcript span').on('click', function(){
        var span = this;
        var text = span.innerText;
        console.log("Clicked on '" + text + "' in the transcript.");

        var index = span.data('index');
        var time = timestamp_map[index];
        if(time){
            $('#audio')[0].currentTime = time;
        }        
    });
});