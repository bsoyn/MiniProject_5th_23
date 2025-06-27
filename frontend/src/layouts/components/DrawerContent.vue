<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
</script>

<template>
    <ul>
        <VerticalNavSectionTitle :item="{ heading: '작가' }" />
        <VerticalNavLink
            :item="{
                title: '작가',
                to: '/authors',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '내 도서 조회',
                to: '/readMyBooks',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '원고 관리' }" />
        <VerticalNavLink
            :item="{
                title: '원고',
                to: '/manuscripts',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '독자 관리' }" />
        <VerticalNavLink
            :item="{
                title: '독자 관리',
                to: '/managerReaders',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '유저 관리' }" />
        <VerticalNavLink
            :item="{
                title: '작가 유저 관리',
                to: '/manageAuthors',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '독자 유저 관리',
                to: '/manageReaderInfos',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '작가 등록 요청 모니터링',
                to: '/requestRegisterMonitorings',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '작가 정보 조회 ',
                to: '/readAuthorInfos',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '독자 정보 조회',
                to: '/readReaderInfos',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '포인트' }" />
        <VerticalNavLink
            :item="{
                title: '포인트관리',
                to: '/points',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '도서 구매 관리' }" />
        <VerticalNavLink
            :item="{
                title: '구매도서',
                to: '/purchasedBooks',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '구독권' }" />
        <VerticalNavLink
            :item="{
                title: '구독권 관리',
                to: '/subscribes',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '결제 처리' }" />
        <VerticalNavLink
            :item="{
                title: '결제 처리',
                to: '/payments',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'AI서비스' }" />
        <VerticalNavLink
            :item="{
                title: '책 표지',
                to: '/bookCovers',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '책 요약',
                to: '/bookSummaries',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '도서 관리' }" />
        <VerticalNavLink
            :item="{
                title: '도서',
                to: '/books',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '도서 열람 관리' }" />
        <VerticalNavLink
            :item="{
                title: '도서 조회',
                to: '/availiableBookLists',
            }"
        />
    </ul>
</template>
