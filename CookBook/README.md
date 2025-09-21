Bu küçük CookBook uygulamasını yazarken Android’de temel ama kritik  şu konuları öğrendim:

Room temelleri: @Entity, @Dao, @Database kurgusunu kurup CRUD akışını baştan sona denedim. Veriyi katmanlı düşünmenin (DAO ↔ ViewModel/Use‑case ↔ UI) neden önemli olduğunu gördüm.

RxJava ile iş parçacığı yönetimi: Schedulers.io() ile işi arka planda yapıp AndroidSchedulers.mainThread() ile UI güncellemenin farkını, Flowable/Completable seçimlerinin ne zaman mantıklı olduğunu pratikte kavradım.

Navigation Component & Safe Args: Ekranlar arası veri taşımayı güvenli ve okunur hâle getirdim; back stack ve yönlendirme mantığını sadeleştirdim.

RecyclerView disiplini: Adapter–ViewHolder yapısını temiz kurup bağlama (bind) işini minimumda tutmanın performansa etkisini gördüm; liste güncellemelerini kontrollü yapmayı öğrendim.

Runtime izinler & Activity Result API: Galeriden görsel seçerken izin akışını kullanıcı dostu çözmeyi, eski onActivityResult yerine modern başlatıcıların hayat kurtardığını deneyimledim.

ViewBinding ile güvenli UI erişimi: Null hatalarını azalttım, id tabanlı kırılmaları engelledim, kod okunabilirliği yükseldi.

Hata ayıklama ve sürüm uyuşmazlıkları: Gradle/KSP sürüm uyarıları ve derleme hatalarıyla uğraşmak; dokümantasyon okumayı, uyarıları anlamlandırmayı ve hızlı çözüm üretmeyi öğretti.

Kısa döngü—hızlı geri bildirim: Küçük parçalarda geliştirme, sık çalıştırma ve log takibiyle hatayı erken yakalamanın projeyi ne kadar rahatlattığını gördüm.

Özetle: Bu proje; veri katmanı (Room) + iş parçacığı yönetimi (RxJava) + modern Android mimari parçaları (Navigation, Activity Result, ViewBinding, RecyclerView) üçgeninde elimi hızlandırdı ve temel taşları pekiştirdi.
