/**
 * 
 */
 $(document).ready(function() {
    $('#nameError').hide();
    $('#name').change(function(){
        var val = $('#name').val();
        $.ajax({
            url : 'validate',
            data : {'ename':val},
            success : function(resp){
                if(resp!=''){
                    $('#nameError').show();
                    $('#nameError').html(resp);
                    $('#nameError').css('color','red')
                    $('#nameError').css('font-weight','bold')
                    $('#button').attr('disabled',true)
                } else{
                    $('#nameError').hide();
                    $('#button').attr('disabled',false)
                }
            }
        });
    });
});