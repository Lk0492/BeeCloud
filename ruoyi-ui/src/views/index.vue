<template>
  <div class="app-container home">
    <!-- 轮播图区域 -->
    <div class="carousel-container">
      <el-carousel :interval="5000" trigger="click" height="400px" indicator-position="outside">
        <el-carousel-item v-for="(banner, index) in bannerList" :key="index">
          <div class="banner-item" :style="{ backgroundImage: `url(${banner})` }"></div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 后续在此添加图表等内容 -->
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      bannerList: [
        require('@/assets/images/banner1.jpg'),
        require('@/assets/images/banner2.jpg'),
        require('@/assets/images/banner3.jpg'),
        require('@/assets/images/banner4.jpg'),
        require('@/assets/images/banner5.jpg')
      ]
    };
  }
}
</script>

<style scoped lang="scss">
.home {
  min-height: 100%;
  padding: 20px;
}

.carousel-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.banner-item {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

@media (max-width: 768px) {
  .carousel-container {
    border-radius: 0;
    margin: 0 0 20px;
  }

  :deep(.el-carousel) {
    height: 250px !important;
  }
}

:deep(.el-carousel__indicators--outside) {
  padding: 15px 0;
}

:deep(.el-carousel__indicator) {
  padding: 0 4px;
}

:deep(.el-carousel__indicator .el-carousel__button) {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.5);
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  background-color: #409EFF;
}
</style>
