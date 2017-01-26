$ ->
  showPicList()

  $('#portfolio_rank #up').click ->
    $('#portfolio_rank #rank').val (Number($('#portfolio_rank #rank').val()) + 1)
  $('#portfolio_rank #down').click ->
    $('#portfolio_rank #rank').val (Number($('#portfolio_rank #rank').val()) - 1)


@showPicList = () ->
  $.post '/admin/portfolio_pic_list',
    {
      pj_idx: $('#pj_idx').val()
      pf_category: $('#pf_category').val()
    }
    (data, status) ->
      $('#portfolio_pics_container').html(data)
      $('.delete_btn').click ->
        $.post '/admin/portfolio_pic_delete',
          {
            idx: $(this).data('idx')
          }
          (data, status) ->
            if (data == 'success')
              showPicList()
            else
              alert '사진 삭제가 실패했습니다.'

@uploadedImageProcess = (result) ->
  if (result.indexOf('@') > -1)
    url = result.replace('@', '')
    $('#pf_thumbnail').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#pf_thumbnail')
    $('#thumbnail_to_submit').val(url)
    boardModule().putPickThumbnailForm()

  if (result.indexOf('$') > -1)
    url = result.replace('$', '')
    $('#pf_in_charge_photo').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#pf_in_charge_photo')
    $('#in_charge_photo_to_submit').val(url)
    boardModule().putPickInChargePhotoForm()

  if (result.indexOf('#') > -1)
    $.post '/admin/portfolio_pics_upload',
      {
        pj_idx: $('#pj_idx').val()
        pf_category: $('#pf_category').val()
        urls: result
      }
      (data, status) ->
        if (data == 'success')
          showPicList()

    boardModule().putPickPicsForm()
