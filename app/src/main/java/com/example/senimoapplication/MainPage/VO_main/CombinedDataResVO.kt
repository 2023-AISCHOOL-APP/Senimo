package com.example.senimoapplication.MainPage.VO_main

import com.example.senimoapplication.Club.VO.ScheduleVO
import com.example.senimoapplication.MainPage.VO_main.MeetingVO

data class CombinedDataResVO(
    val mySchedule: List<ScheduleVO>,
    val myClub: List<MeetingVO>,
    val myInterestedClub: List<MeetingVO>
)