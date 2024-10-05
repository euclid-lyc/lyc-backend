package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.enums.*;
import euclid.lyc_spring.domain.info.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class InfoResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class AllInfoDTO {

        private final Long memberId;
        private final PhysicalSpecDTO spec;
        private final StyleDTO preferredStyle;
        private final StyleDTO nonPreferredStyle;
        private final MaterialDTO preferredMaterial;
        private final MaterialDTO nonPreferredMaterial;
        private final FitDTO preferredFit;
        private final FitDTO nonPreferredFit;
        private final BodyTypeDTO goodBodyType;
        private final BodyTypeDTO badBodyType;
        private final String details;

        public static AllInfoDTO toDTO(Info info) {
            return AllInfoDTO.builder()
                    .memberId(info.getMember().getId())
                    .spec(PhysicalSpecDTO.toDTO(info))
                    .preferredStyle(StyleDTO.toDTO(info.getInfoStyleList().stream()
                            .filter(InfoStyle::getIsPrefer)
                            .toList()))
                    .nonPreferredStyle(StyleDTO.toDTO(info.getInfoStyleList().stream()
                            .filter(infoStyle -> !infoStyle.getIsPrefer())
                            .toList()))
                    .preferredMaterial(MaterialDTO.toDTO(info.getInfoMaterialList().stream()
                            .filter(InfoMaterial::getIsPrefer)
                            .toList()))
                    .nonPreferredMaterial(MaterialDTO.toDTO(info.getInfoMaterialList().stream()
                            .filter(infoMaterial -> !infoMaterial.getIsPrefer())
                            .toList()))
                    .preferredFit(FitDTO.toDTO(info.getInfoFitList().stream()
                            .filter(InfoFit::getIsPrefer)
                            .toList()))
                    .nonPreferredFit(FitDTO.toDTO(info.getInfoFitList().stream()
                            .filter(infoFit -> !infoFit.getIsPrefer())
                            .toList()))
                    .goodBodyType(BodyTypeDTO.toDTO(info.getInfoBodyTypeList().stream()
                            .filter(InfoBodyType::getIsGood)
                            .toList()))
                    .badBodyType(BodyTypeDTO.toDTO(info.getInfoBodyTypeList().stream()
                            .filter(infoBodyType -> !infoBodyType.getIsGood())
                            .toList()))
                    .details(info.getText())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PhysicalSpecDTO {

        private final Short height;
        private final Short weight;
        private final TopSize topSize;
        private final BottomSize bottomSize;

        public static PhysicalSpecDTO toDTO(Info info) {
            return PhysicalSpecDTO.builder()
                    .height(info.getHeight())
                    .weight(info.getWeight())
                    .topSize(info.getTopSize())
                    .bottomSize(info.getBottomSize())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class StyleDTO {

        private final List<Style> styles;

        public static StyleDTO toDTO(List<InfoStyle> infoStyles) {
            return StyleDTO.builder()
                    .styles(infoStyles.stream()
                            .map(InfoStyle::getStyle)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MaterialDTO {

        private final List<Material> materials;

        public static MaterialDTO toDTO(List<InfoMaterial> infoMaterials) {
            return MaterialDTO.builder()
                    .materials(infoMaterials.stream()
                            .map(InfoMaterial::getMaterial)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class FitDTO {

        private final List<Fit> fits;

        public static FitDTO toDTO(List<InfoFit> infoFits) {
            return FitDTO.builder()
                    .fits(infoFits.stream()
                            .map(InfoFit::getFit)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class BodyTypeDTO {

        private final List<BodyType> bodyTypes;

        public static BodyTypeDTO toDTO(List<InfoBodyType> infoBodyTypes) {
            return BodyTypeDTO.builder()
                    .bodyTypes(infoBodyTypes.stream()
                            .map(InfoBodyType::getBodyType)
                            .toList())
                    .build();
        }
    }
}
