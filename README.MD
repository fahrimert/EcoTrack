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
