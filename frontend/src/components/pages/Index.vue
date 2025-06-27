<template>
  <v-container fluid>
    <v-row>
      <v-card
          v-for="(card, index) in cards"
          :key="index"
          class="mx-auto"
          style="height:300px; width:300px; margin-bottom:20px;"
          outlined
      >
        <v-list-item>
          <v-icon style="margin-top: 30%; margin-left: 37.5%;" color="primary" size="64">mdi-apps</v-icon>
        </v-list-item>
        <v-card-actions>
          <v-btn
              class="mx-auto"
              outlined
              rounded
              :to="card.link"
              style="font-weight:500; font-size:20px; border:solid 2px; max-width:250px; overflow:hidden; margin-top: 10%;"
          >
              {{ card.text }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Index',
  data: () => ({
    cards: [
      {
          text: "작가",
          link: "/authors",
      },
      {
          text: "내 도서 조회",
          link: "/readMyBooks",
      },
      {
          text: "원고",
          link: "/manuscripts",
      },
      {
          text: "독자 관리",
          link: "/managerReaders",
      },
      {
          text: "작가 유저 관리",
          link: "/manageAuthors",
      },
      {
          text: "독자 유저 관리",
          link: "/manageReaderInfos",
      },
      {
          text: "작가 등록 요청 모니터링",
          link: "/requestRegisterMonitorings",
      },
      {
          text: "작가 정보 조회 ",
          link: "/readAuthorInfos",
      },
      {
          text: "독자 정보 조회",
          link: "/readReaderInfos",
      },
      {
          text: "포인트관리",
          link: "/points",
      },
      {
          text: "구매도서",
          link: "/purchasedBooks",
      },
      {
          text: "구독권 관리",
          link: "/subscribes",
      },
      {
          text: "결제 처리",
          link: "/payments",
      },
      {
          text: "책 표지",
          link: "/bookCovers",
      },
      {
          text: "책 요약",
          link: "/bookSummaries",
      },
      {
          text: "도서",
          link: "/books",
      },
      {
          text: "도서 조회",
          link: "/availiableBookLists",
      },
    ],
  }),
};
</script>
