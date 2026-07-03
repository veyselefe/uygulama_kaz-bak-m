# Kaz Bakım - Android Uygulaması

Kaz bakımı için nöbet vakitleri, nöbet sırası, yem ve su zamanlarını yöneten modern Android uygulaması.

## Özellikler

- **Nöbet Vakitleri**: Kişilere göre nöbet saatlerini ve tarihlerini yönetin
- **Nöbet Sırası**: Nöbetçi sırasını oluşturun ve düzenleyin
- **Yem Vakitleri**: Kazlara yem verme zamanlarını ayarlayın
- **Su Vakitleri**: Kazlara su değiştirme zamanlarını ayarlayın
- **Alarm Sistemi**: Belirlenen zamanlarda otomatik alarm çalar
- **Modern UI**: Jetpack Compose ile şık ve kullanıcı dostu arayüz
- **Kalıcı Depolama**: Room Database ile verileriniz kaybolmaz

## Teknolojiler

- **Kotlin**: Modern Android geliştirme dili
- **Jetpack Compose**: Declarative UI framework
- **Room Database**: Yerel veri depolama
- **Material Design 3**: Modern tasarım sistemi
- **Coroutines & Flow**: Asenkron programlama

## Gereksinimler

- Android Studio Hedgehog (2023.1.1) veya üzeri
- JDK 17
- Android SDK 34
- Minimum Android API: 24 (Android 7.0)

## Kurulum

1. Projeyi Android Studio'da açın
2. Gradle sync yapın
3. Emülatör veya fiziksel cihaz seçin
4. Run butonuna basın

## İzinler

Uygulama şu izinleri gerektirir:
- `SCHEDULE_EXACT_ALARM`: Tam zamanlı alarm için
- `POST_NOTIFICATIONS`: Bildirim göndermek için (Android 13+)
- `RECEIVE_BOOT_COMPLETED`: Cihaz yeniden başlatıldığında alarmları yeniden kurmak için

## Kullanım

### Nöbet Vakitleri
1. Sağ alttaki + butonuna tıklayın
2. Kişi adı, başlangıç saati, bitiş saati ve tarih girin
3. Alarm otomatik olarak ayarlanır

### Nöbet Sırası
1. Kişileri ekleyin
2. Yukarı/aşağı okları ile sırayı düzenleyin
3. Sıra otomatik olarak kaydedilir

### Yem/Su Vakitleri
1. Saat girin (HH:mm formatında)
2. İsteğe bağlı açıklama ekleyin
3. Alarm otomatik olarak ayarlanır

## Proje Yapısı

```
app/
├── src/main/java/com/kazbakim/
│   ├── data/              # Veri katmanı
│   │   ├── entity/        # Veri modelleri
│   │   ├── dao/           # Database işlemleri
│   │   └── AppDatabase.kt # Room veritabanı
│   ├── viewmodel/         # ViewModels
│   ├── ui/
│   │   ├── screens/       # Composable ekranlar
│   │   └── theme/         # Tema ve renkler
│   ├── service/           # Alarm servisi
│   ├── receiver/          # Broadcast receiver
│   └── MainActivity.kt    # Ana aktivite
└── build.gradle.kts       # Gradle build dosyası
```

## Geliştirme

Bu proje açık kaynaklıdır ve katkılarınızı bekliyoruz.

## Lisans

MIT License
