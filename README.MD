# EcoTrack - Sensör Arıza Takip ve Çözüm Platformu

EcoTrack, saha ekiplerinin sensör arızalarını tespit edip çözebildiği, rol tabanlı ve gerçek zamanlı bir görev takip sistemidir.  
İşçi, süpervizör ve müdür rollerine sahip kullanıcılar; harita, görev, bildirim ve raporlama gibi modüllerle sistemde aktif rol oynar.

---

## Kullanıcı Rolleri

- İşçi (Worker): Sensör arızalarını sahada tespit edip çözen kullanıcı.  
- Süpervizör (Supervisor): İşçileri yöneten, görev atayan ve performanslarını izleyen kullanıcı.  
- Müdür (Manager): Sistemde genel yetkilere sahip, analiz ve kullanıcı yönetimi yapan kullanıcı.

---

## Giriş ve Yetkilendirme

- Kullanıcılar PostgreSQL veritabanında kayıtlıdır.  
- Kimlik doğrulama JWT token ile sağlanır.  
- Middleware aracılığıyla rol bazlı yönlendirme yapılır:


---

## İşçi Paneli Özellikleri

- Ana Sayfa:  
  - Canlı harita üzerinde işçi ve sensör konumları (Google Maps API).  
  - Sensör listesi ve durumları renk kodlarıyla gösterilir.  
  - Diğer işçilerin aldığı sensörler bulanık gösterilir.  
  - WebSocket üzerinden gelen görev bildirimleri.

- Sensör Görevi Akışı:  
  - Aynı anda sadece 1 sensörle çalışılabilir.  
  - "Gidin" butonuna tıklandığında sensör atanır ve detaylar açılır.  
  - Görev detay sayfasında arıza açıklaması girilir, çözüm resmi yüklenir, mini harita ve konum güncellenir, sensör durumu güncellenir.  
  - Görev tamamlandığında sensör geçmiş çözülenler listesine aktarılır.

- Çözüm Geçmişi:  
  - İşçinin geçmişte çözdüğü sensörler listelenir.  
  - Her çözüm için detay sayfası (açıklama, tarih, konum, resim galerisi) vardır.

- Ekip Takibi:  
  - Harita üzerinden diğer işçilerin ve aktif görevlerin konumları canlı olarak gösterilir.

---

## Süpervizör Paneli Özellikleri

- Dashboard:  
  - Doughnut grafiği ile görev dışı sensör statü dağılımı.  
  - Bar chart ile zaman bazlı (günlük, haftalık, aylık) çözüm sayıları.  
  - Heatmap ile hatalı sensör konumlarının sıcaklık haritası.  
  - WebSocket ile canlı işçi listesi.

- Rapor Sayfası:  
  - Takvim üzerinden gün seçilip sensör çözüm raporları görüntülenir.  
  - Raporlar PDF olarak indirilebilir veya yöneticilere gönderilebilir.

- Performans Analizi:  
  - Seçilen işçi için performans çizgi grafiği, sensör türü donut grafiği, takvimsel çözüm yoğunluğu ve konuma dayalı sıcaklık haritası.  
  - Genel lider tablosu (en kısa sürede çözüm, en çok çözüm vb.).

- Görev Atama:  
  - İşçi ve sensör seçilerek görev tanımlanır.  
  - Harita üzerinden rota çizimi yapılır.  
  - WebSocket ile görev bildirimi anlık gönderilir.  
  - Atanan görevler listelenir ve çözüm süreci takip edilir.

---

## Müdür Paneli Özellikleri

- Dashboard:  
  - Görev çözüm oranları (süpervizör bazlı).  
  - İşçi ortalama çözüm süreleri.  
  - Radar grafiklerle detaylı analizler.  
  - Tüm görevlerde sensör statü dağılımı.

- Kullanıcı Yönetimi:  
  - Tüm kullanıcılar rol bazlı listelenir ve filtrelenir.  
  - Kullanıcı askıya alma veya silme işlemleri.  
  - İşçi ve süpervizör lider tabloları.

---

## Sensör Yönetimi

- Sensör ekleme, güncelleme, silme.  
- Sensör konumu harita destekli güncellenir.  
- Duruma göre filtreleme ve detaylı sensör sayfaları.

---

## Süpervizör PDF Raporları

- Her süpervizörün gönderdiği raporlar listelenir.  
- PDF formatında indirilebilir.

---

## Gerçek Zamanlı Özellikler

- WebSocket tabanlı görev ve sistem bildirimleri anlık iletilir.  
- Harita üzerindeki işçi ve sensör konumları canlı olarak güncellenir.  
- Görev ve çözüm süreçleri tüm roller için otomatik senkronize edilir.

---
## Test Kapsamı

Proje içerisinde backend katmanındaki önemli modüller için kapsamlı entegrasyon testleri yazılmıştır. Testler hem **happy path** senaryolarını hem de **edge case** durumlarını içermektedir. Bu sayede uygulamanın güvenilirliği ve hata dayanıklılığı artırılmıştır.

### Test Edilen Modüller ve Kapsamları

- **Auth Controller**  
  - Kullanıcı kimlik doğrulama akışlarının tüm ana senaryoları  
  - Hatalı giriş, yetkisiz erişim gibi edge case durumları  
  - Token oluşturma, yenileme ve geçersiz token kontrolleri  

- **Integration Servis & Repository & Manager**  
  - Veri katmanının doğru çalışması  
  - Servislerin iş mantığının doğrulanması  
  - Repository ile veri tabanı etkileşimlerinin test edilmesi  

- **Controller, Service, Integration Testleri**  
  - **Notification Modülü:** Bildirim gönderim mekanizmasının doğru tetiklenmesi ve edge case senaryoları  
  - **Sensor Modülü:** Sensör verilerinin doğruluğu, sensör ekleme, güncelleme ve silme işlemlerinin testi  
  - **Supervizör ve User Rolleri:**  
    - Yetkilendirme kontrolleri  
    - Rol bazlı erişim testleri  
    - Kullanıcı ve süpervizör işlemlerinde happy path ve edge case senaryolarının kapsamlı testi  

### Test Süreci ve Araçları

- Testler, JUnit ve Mockito kullanılarak yazılmıştır.  
- Mocklama ile servis bağımlılıkları izole edilmiştir.  

---

Bu kapsamlı test yaklaşımı, uygulamanın beklenen işleyişine uygunluğunu garanti altına alırken olası hata senaryolarında da sağlamlık sağlar.

## Teknoloji Stack

### 🌐 Frontend

**Çekirdek Teknolojiler**  
- Framework: Next.js v13.4+ (App Router)  
- Dil: TypeScript v4.9+  

**Durum Yönetimi**  
- React Context API  
- Zustand (Hafif global state yönetimi)  

**UI & Stil**  
- Bileşen Kütüphaneleri:  
  - Chakra UI v2.8+  
  - shadcn/ui (Radix tabanlı)  
- CSS Framework:  
  - Tailwind CSS v3.3+  
  - CSS Modules  

**Veri Görselleştirme**  
- Grafikler: Recharts v2.6+  
- Haritalar:  
  - Google Maps API  
  - @react-google-maps/api  

**Form Yönetimi**  
- React Hook Form v7.43+  
- Zod (Schema validation)  
- Zod Resolver (Form validation)  

**Veri İşleme**  
- Axios (HTTP istekleri)  
- date-fns (Tarih işlemleri)  
- html2pdf.js (PDF oluşturma)  

**Tablo ve Veri Gösterimi**  
- TanStack Table (React Table)  
- React Data Grid  

---

### ⚙️ Backend

**Çekirdek Teknolojiler**  
- Framework: Spring Boot v3.1+  
- Dil: Java 17+  
- Build Tool: Maven  

**Veritabanı**  
- PostgreSQL 15+ (Ana veritabanı)  
- Hibernate ORM (JPA implementasyonu)  
- Liquibase (Database migration)  

**Güvenlik**  
- JWT (Kimlik doğrulama)  
- Spring Security  
- CORS Yapılandırması  

**API Özellikleri**  
- RESTful API tasarımı  
- WebSocket (Gerçek zamanlı bildirimler)  
- Swagger/OpenAPI (API dokümantasyonu)  

**Diğer Kütüphaneler**  
- ModelMapper (DTO dönüşümleri)  
- Lombok (Boilerplate kod azaltma)  
- Java Mail Sender (E-posta işlemleri)  

---

### 🛠️ DevOps & Araçlar

**Geliştirme Araçları**  
- Docker (Containerization)  
- Postman (API testleri)  
- IntelliJ IDEA (Java IDE)  
- VS Code (Frontend IDE)  
