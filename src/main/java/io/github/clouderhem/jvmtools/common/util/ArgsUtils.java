package io.github.clouderhem.jvmtools.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron Yeung
 * @date 8/21/2023 4:31 PM
 */
public class ArgsUtils {

    public static List<String> buildArgList(String args, String separator) {
        return Arrays.stream(args.split(separator)).map(String::trim)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
