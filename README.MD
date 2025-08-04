EcoTrack - Sensör Arıza Takip ve Çözüm Platformu
EcoTrack, saha ekiplerinin sensör arızalarını tespit edip çözebildiği, rol tabanlı ve gerçek zamanlı bir görev takip sistemidir. İşçi, süpervizör ve müdür rollerine sahip kullanıcılar; harita, görev, bildirim ve raporlama gibi modüllerle sistemde aktif rol oynar.

 Kullanıcı Rolleri
 İşçi (Worker): Sensör arızalarını sahada tespit edip çözen kullanıcı.

 Süpervizör (Supervisor): İşçileri yöneten, görev atayan ve performanslarını izleyen kullanıcı.

 Müdür (Manager): Sistemde genel yetkilere sahip, analiz ve kullanıcı yönetimi yapan kullanıcı.

 Giriş ve Yetkilendirme
PostgreSQL veritabanında kayıtlı kullanıcılar giriş yapabilir.

Kimlik doğrulama JWT token ile yapılır.

Middleware aracılığıyla kullanıcı rolüne göre yönlendirme gerçekleştirilir:

ROLE_WORKER → /worker/dashboard

ROLE_SUPERVISOR → /supervisor/dashboard

ROLE_MANAGER → /manager/dashboard

 İşçi Paneli Özellikleri
Ana Sayfa
Canlı harita üzerinde işçi ve sensör konumları görüntülenir (Google Maps API).

Sensör listesi ve durumları renk kodlarıyla belirtilir.

Diğer işçilerin aldığı sensörler bulanık şekilde gösterilir.

Bildirim paneli: WebSocket üzerinden gelen görev bildirileri.

🔧 Sensör Görevi Akışı
Her işçi aynı anda yalnızca 1 sensörle çalışabilir.

“Gidin” butonuna tıklandığında sensör atanır ve detayları açılır.

Sensör detay sayfasında:

Arıza açıklaması girişi

Çözüm resmi yükleme

Mini harita ve konum güncelleme

Sensör durumu güncelleme

Görev tamamlandığında sensör “geçmiş çözülenler” listesine aktarılır.

Çözüm Geçmişi
İşçinin geçmişte çözdüğü sensörleri listeler.

Her çözüm için detay sayfası: açıklama, tarih, konum ve resim galerisi.

 Ekip Takibi
Harita üzerinden diğer işçilerin ve aktif görevlerinin konumları canlı olarak gösterilir.

 Süpervizör Paneli Özellikleri
 Dashboard
Doughnut Grafiği: Görev dışı sensör statü dağılımı.

Bar Chart: Zaman bazlı (günlük, haftalık, aylık) çözüm sayıları.

Heatmap: Hatalı sensör konumlarının sıcaklık haritası.

Canlı İşçi Listesi: Online olan işçilerin listesi (WebSocket ile gerçek zamanlı).

 Rapor Sayfası
Takvim üzerinden gün seçilip sensör çözüm raporları görüntülenir.

Raporlar PDF olarak indirilebilir veya yöneticilere gönderilebilir.

 Performans Analizi Sayfası
Seçilen işçi için:

Performans çizgi grafiği

Sensör türü donut grafiği

Takvimsel çözüm yoğunluğu

Konuma dayalı sıcaklık haritası

Genel lider tablosu (en kısa sürede çözüm, en çok çözüm vb.)

 Görev Atama
Süpervizör:

İşçi ve sensör seçerek görev tanımlar.

Harita üzerinden rota çizimi yapılır.

WebSocket ile görev bildirimi işçiye gönderilir.

Atanan görevler anlık olarak listelenir ve çözüm süreci izlenir.

 Müdür Paneli Özellikleri
 Dashboard
Görev Çözüm Oranı: Süpervizörlerin zaman bazlı görev çözüm oranları.

İşçi Ort. Süreleri: Görevlerin ortalama çözüm süresi.

Radar Grafikler: Süpervizör ve işçilere ait detaylı analizler.

Sensör Statü Dağılımı: Tüm görevlerdeki sensör durumlarını gösterir.

 Kullanıcı Yönetimi
Tüm kullanıcıları rol bazlı listeleme ve filtreleme.

Kullanıcıyı askıya alma veya sistemden silme.

İşçi ve süpervizör lider tabloları.

🛰️ Sensör Yönetimi
Sensör ekleme, güncelleme, silme.

Sensör konumu güncelleme (harita destekli).

Duruma göre filtreleme ve detaylı sensör sayfaları.

 Süpervizör PDF Raporları
Her süpervizörün gönderdiği raporları listeleme ve PDF olarak indirme.

 Gerçek Zamanlı Özellikler
WebSocket Tabanlı Bildirimler: Görev ve sistem bildirimleri anlık olarak iletilir.

Konum Güncellemeleri: Harita üzerindeki işçi/sensör konumları canlı olarak gösterilir.

Otomatik Senkronizasyon: Görev ve çözüm süreçleri tüm roller için eş zamanlı güncellenir.
