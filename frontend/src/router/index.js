import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/authors',
      component: () => import('../components/ui/AuthorGrid.vue'),
    },
    {
      path: '/readMyBooks',
      component: () => import('../components/ReadMyBooksView.vue'),
    },
    {
      path: '/manuscripts',
      component: () => import('../components/ui/ManuscriptGrid.vue'),
    },
    {
      path: '/managerReaders',
      component: () => import('../components/ui/ManagerReaderGrid.vue'),
    },
    {
      path: '/manageAuthors',
      component: () => import('../components/ui/ManageAuthorGrid.vue'),
    },
    {
      path: '/manageReaderInfos',
      component: () => import('../components/ui/ManageReaderInfoGrid.vue'),
    },
    {
      path: '/requestRegisterMonitorings',
      component: () => import('../components/RequestRegisterMonitoringView.vue'),
    },
    {
      path: '/readAuthorInfos',
      component: () => import('../components/ReadAuthorInfoView.vue'),
    },
    {
      path: '/readReaderInfos',
      component: () => import('../components/ReadReaderInfoView.vue'),
    },
    {
      path: '/points',
      component: () => import('../components/ui/PointGrid.vue'),
    },
    {
      path: '/purchasedBooks',
      component: () => import('../components/ui/PurchasedBookGrid.vue'),
    },
    {
      path: '/subscribes',
      component: () => import('../components/ui/SubscribeGrid.vue'),
    },
    {
      path: '/payments',
      component: () => import('../components/ui/PaymentGrid.vue'),
    },
    {
      path: '/bookCovers',
      component: () => import('../components/ui/BookCoverGrid.vue'),
    },
    {
      path: '/bookSummaries',
      component: () => import('../components/ui/BookSummaryGrid.vue'),
    },
    {
      path: '/books',
      component: () => import('../components/ui/BookGrid.vue'),
    },
    {
      path: '/availiableBookLists',
      component: () => import('../components/AvailiableBookListView.vue'),
    },
  ],
})

export default router;
