package ru.test.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PostDto (
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val groupId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)