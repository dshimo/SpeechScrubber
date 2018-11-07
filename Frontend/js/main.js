$(document).ready(function() {
    let upload_url = "";
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

    // Upon clicking the file upload icon, click the hidden file input to open up a finder.
    $('#file_upload').on('click', function(event){
        event.stopPropagation();
        $('#file_browse').click();
    });
});