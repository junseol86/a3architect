$ ->
  $('#category_custome_input').click ->
    $('#category_custome_radio').prop 'checked', true
  $('#category_custome_input').change ->
    $('#category_custome_radio').val $(this).val()

  showPicList()

@submitContents = () ->
  $('#modified_input').val(new Date().getTimeString())
  document.sites_write.submit()

@showPicList = () ->
  $.post '/admin/sites_scene_list',
    {
      pj_idx: $('#pj_idx').val()
    }
    (data, status) ->
      $('#sites_scene_container').html(data)
      $('.delete_btn').click ->
        $.post '/admin/sites_pic_delete',
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
    $('#sts_thumbnail').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#sts_thumbnail')
    $('#thumbnail_to_submit').val(url)
    boardModule().putPickThumbnailForm()

  if (result.indexOf('$') > -1)
    url = result.replace('$', '')
    $('#sts_in_charge_photo').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#sts_in_charge_photo')
    $('#in_charge_photo_to_submit').val(url)
    boardModule().putPickInChargePhotoForm()

  if (result.indexOf('#') > -1)
    $.post '/admin/sites_scene_upload',
      {
        pj_idx: $('#pj_idx').val()
        category: $('#scene_btns input[type="radio"]:checked').val()
        title: $('#pic_title').val()
        urls: result.replace('#', '')
      }
      (data, status) ->
        $('#pic_title').val ''
        showPicList()
    boardModule().putPickSceneForm()
