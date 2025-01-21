private void ratio() {
        chamber = new ArrayList<>();
        Random rand = new Random();

        // À’e‚ğÅ’á‚Å‚à1‚Â”z’u
        chamber.add(1); // À’e1‚Â

        // c‚è5‚Â‚ğƒ‰ƒ“ƒ_ƒ€‚ÉÀ’e‚Ü‚½‚Í‹ó’e‚Éİ’è
        int remainingBulletCount = 5;
        for (int i = 0; i < remainingBulletCount; i++) {
            chamber.add(rand.nextInt(2)); // 0‚Ü‚½‚Í1i‹ó’e‚Ü‚½‚ÍÀ’ej
        }

        Collections.shuffle(chamber); // ’e‚Ì‡”Ô‚ğƒ‰ƒ“ƒ_ƒ€‚É•À‚×‘Ö‚¦
    }